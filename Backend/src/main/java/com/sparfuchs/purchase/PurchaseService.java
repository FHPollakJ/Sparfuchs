package com.sparfuchs.purchase;

import com.sparfuchs.DTO.*;
import com.sparfuchs.exception.BadRequestException;
import com.sparfuchs.exception.ForbiddenException;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.product.Product;
import com.sparfuchs.product.ProductRepository;
import com.sparfuchs.product.ProductService;
import com.sparfuchs.purchaseProduct.PurchaseProduct;
import com.sparfuchs.purchaseProduct.PurchaseProductRepository;
import com.sparfuchs.store.Store;
import com.sparfuchs.store.StoreRepository;
import com.sparfuchs.storeProduct.StoreProduct;
import com.sparfuchs.storeProduct.StoreProductPriceHistory;
import com.sparfuchs.storeProduct.StoreProductPriceHistoryRepository;
import com.sparfuchs.storeProduct.StoreProductRepository;
import com.sparfuchs.user.User;
import com.sparfuchs.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StoreProductRepository storeProductRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final StoreProductPriceHistoryRepository storeProductPriceHistoryRepository;

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            StoreProductRepository storeProductRepository,
            PurchaseProductRepository purchaseProductRepository,
            UserRepository userRepository,
            ProductService productService,
            StoreProductPriceHistoryRepository storeProductPriceHistoryRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.storeProductRepository = storeProductRepository;
        this.purchaseProductRepository = purchaseProductRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.storeProductPriceHistoryRepository = storeProductPriceHistoryRepository;
    }

    @Transactional
    public PurchaseDTO startPurchase(StartPurchaseDTO request, long userId) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new NotFoundException("Store not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Purchase purchase = new Purchase(user, store, LocalDateTime.now());
        purchaseRepository.save(purchase);
        return new PurchaseDTO(purchase.getId(),request.storeId(),request.createdAt(), new ArrayList<>(),false,0,0);
    }

    @Transactional
    public PurchaseDTO addProductToPurchase(PurchaseProductDTO request, long userId) {
        Purchase purchase = getValidatedEditablePurchase(request.purchaseId(), userId);

        if(Objects.equals(request.barcode(), "NOBARCODE")){
            return addNoBarcodeProductToPurchase(request,userId);
        }
        Product product = productRepository.findByBarcode(request.barcode())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product, purchase.getStore().getId())
                .orElseThrow(() -> new NotFoundException("StoreProduct not found"));

        PurchaseProduct purchaseProduct = productService.createPurchaseProduct(
                purchase,
                product,
                request.quantity(),
                request.discount(),
                storeProduct.getProduct().getName(),
                storeProduct.getPrice()
        );

        purchaseProductRepository.save(purchaseProduct);
        purchase.getProducts().add(purchaseProduct);
        return new PurchaseDTO(purchase.getId(),
                purchase.getStore().getId(),
                LocalDateTime.now(),
                purchaseProductsToDTO(purchase.getProducts()),
                purchase.isCompleted(),
                purchase.getTotalSpent(),
                purchase.getTotalSaved());
    }

    @Transactional
    public PurchaseDTO editProductInPurchase(EditPurchaseProductDTO request, long userId) {
        Purchase purchase = getValidatedEditablePurchase(request.purchaseId(), userId);

        PurchaseProduct targetPurchaseProduct = null;
        Product targetProduct = null;

        for (PurchaseProduct p : purchase.getProducts()) {
            if (p.getId() == request.id()) {
                targetPurchaseProduct = p;
                targetProduct = p.getProduct();
                break;
            }
        }

        if (targetPurchaseProduct == null) {
            throw new NotFoundException("Product not found in purchase.");
        }

        if (targetProduct != null) {
            updateStoreProductPriceIfNeeded(targetProduct, purchase.getStore().getId(), request.price());
        }

        targetPurchaseProduct.setPrice(request.price());
        targetPurchaseProduct.setQuantity(request.quantity());
        targetPurchaseProduct.setDiscountPercent(request.discount());

        //  update  price for all other prducts  that have the same Product
        if (targetProduct != null) {
            for (PurchaseProduct p : purchase.getProducts()) {
                if (p.getId() != request.id() &&
                        p.getProduct() != null &&
                        p.getProduct().getBarcode().equals(targetProduct.getBarcode())) {

                    p.setPrice(request.price());
                }
            }
        }

        return new PurchaseDTO(purchase.getId(),
                purchase.getStore().getId(),
                LocalDateTime.now(),
                purchaseProductsToDTO(purchase.getProducts()),
                purchase.isCompleted(),
                purchase.getTotalSpent(),
                purchase.getTotalSaved());
    }

    private void updateStoreProductPriceIfNeeded(Product product, long storeId, double newPrice) {
        storeProductRepository.findByProductAndStoreId(product, storeId).ifPresent(storeProduct -> {
            if (storeProduct.getPrice() != newPrice) {
                StoreProductPriceHistory priceHistory = new StoreProductPriceHistory(
                        storeProduct,
                        storeProduct.getPrice(),
                        storeProduct.getLastUpdated(),
                        LocalDateTime.now()
                );
                storeProductPriceHistoryRepository.save(priceHistory);
                storeProduct.setPrice(newPrice);
                storeProduct.setLastUpdated(LocalDateTime.now());
                storeProductRepository.save(storeProduct);
            }
        });
    }


    @Transactional
    public void finishPurchase(long purchaseId, long userId) {
        Purchase purchase = getValidatedEditablePurchase(purchaseId, userId);
        purchase.complete();
    }

    @Transactional
    public void deleteIncompletePurchase(long purchaseId, long userId) {
        Purchase purchase = getValidatedEditablePurchase(purchaseId, userId);
        purchaseRepository.delete(purchase);
    }

    @Transactional
    public PurchaseDTO removeProductFromPurchase(PurchaseProductDTO request, long userId) {
        Purchase purchase = getValidatedEditablePurchase(request.purchaseId(), userId);

        PurchaseProduct productToRemove = purchase.getProducts().stream()
                .filter(p -> p.getName().equals(request.productName())
                        && p.getDiscountPercent() == request.discount()
                        && p.getQuantity() == request.quantity())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found in purchase."));

        purchase.getProducts().remove(productToRemove);
        purchaseProductRepository.delete(productToRemove);

        return new PurchaseDTO(
                purchase.getId(),
                purchase.getStore().getId(),
                LocalDateTime.now(),
                purchaseProductsToDTO(purchase.getProducts()),
                purchase.isCompleted(),
                purchase.getTotalSpent(),
                purchase.getTotalSaved()
        );
    }


    @Transactional
    public PurchaseDTO addNoBarcodeProductToPurchase(PurchaseProductDTO request, long userId) {
        Purchase purchase = getValidatedEditablePurchase(request.purchaseId(), userId);

        PurchaseProduct purchaseProduct = productService.createPurchaseProductWithoutBarcode(
                purchase,
                request.quantity(),
                request.discount(),
                request.productName(),
                request.price()
        );

        purchaseProductRepository.save(purchaseProduct);
        purchase.getProducts().add(purchaseProduct);

        return new PurchaseDTO(
                purchase.getId(),
                purchase.getStore().getId(),
                LocalDateTime.now(),
                purchaseProductsToDTO(purchase.getProducts()),
                purchase.isCompleted(),
                purchase.getTotalSpent(),
                purchase.getTotalSaved()
        );
    }

    public List<PurchaseProductResponseDTO> purchaseProductsToDTO(List<PurchaseProduct> purchaseProducts) {
        if (purchaseProducts.isEmpty()) {
            return new ArrayList<>();
        }
        List<PurchaseProductResponseDTO> purchaseProductDTO = new ArrayList<>();
        for(PurchaseProduct product : purchaseProducts){
            PurchaseProductResponseDTO dto = new PurchaseProductResponseDTO(
                    product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getDiscountPercent(),
                    product.getPrice()
            );
            purchaseProductDTO.add(dto);
        }
        return purchaseProductDTO;
    }

    private Purchase getValidatedEditablePurchase(Long purchaseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        if (!purchase.getUser().equals(user)) {
            throw new ForbiddenException("You are not allowed to access this purchase.");
        }

        if (purchase.isCompleted()) {
            throw new BadRequestException("Purchase is already completed.");
        }

        return purchase;
    }

    public List<PurchaseDTO> getPurchasesForUser(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Purchase> purchases = user.getPurchases();
        if (purchases.isEmpty()) {
            return new ArrayList<>();
        }
        List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
        for(Purchase purchase : purchases){
            PurchaseDTO dto = new PurchaseDTO(purchase.getId(),
                    purchase.getStore().getId(),
                    purchase.getCreatedAt(),
                    purchaseProductsToDTO(purchase.getProducts()),
                    purchase.isCompleted(),
                    purchase.getTotalSpent(),
                    purchase.getTotalSaved());
            purchaseDTOS.add(dto);
        }
        return purchaseDTOS;
    }

    public PurchaseDTO getPurchase(PurchaseIdDTO request, long userId) {
        Purchase purchase = getValidatedEditablePurchase(request.purchaseId(), userId);
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getStore().getId(),
                LocalDateTime.now(),
                purchaseProductsToDTO(purchase.getProducts()),
                purchase.isCompleted(),
                purchase.getTotalSpent(),
                purchase.getTotalSaved()
        );
    }
}
