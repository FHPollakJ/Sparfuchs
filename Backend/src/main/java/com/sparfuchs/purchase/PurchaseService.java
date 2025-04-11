package com.sparfuchs.purchase;

import com.sparfuchs.DTO.AddNoBarcodeProductDTO;
import com.sparfuchs.DTO.SaveUnknownProductDTO;
import com.sparfuchs.DTO.AddProductToPurchaseDTO;
import com.sparfuchs.DTO.StartPurchaseDTO;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.product.Product;
import com.sparfuchs.product.ProductRepository;
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

@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;
    private StoreRepository storeRepository;
    private StoreProductRepository storeProductRepository;
    private PurchaseProductRepository purchaseProductRepository;
    private StoreProductPriceHistoryRepository storeProductPriceHistoryRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductRepository productRepository, StoreRepository storeRepository, StoreProductRepository storeProductRepository, PurchaseProductRepository purchaseProductRepository, StoreProductPriceHistoryRepository storeProductPriceHistoryRepository, UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.storeProductRepository = storeProductRepository;
        this.purchaseProductRepository = purchaseProductRepository;
        this.storeProductPriceHistoryRepository = storeProductPriceHistoryRepository;
        this.userRepository = userRepository;
    }

    public Purchase startPurchase(StartPurchaseDTO request, long userId) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundException("Store not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Purchase purchase = new Purchase(user,store,LocalDateTime.now());
        return purchaseRepository.save(purchase);
    }

    public StoreProduct saveUnknownProduct(SaveUnknownProductDTO request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundException("Store not found"));

        Product product = new Product(request.getBarcode(),request.getProductName());
        product = productRepository.save(product);

        StoreProduct storeProduct = new StoreProduct(product,store,request.getPrice(),LocalDateTime.now());
        return storeProductRepository.save(storeProduct);
    }

    public Purchase addProductToPurchase(AddProductToPurchaseDTO request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        Product prd = productRepository.findByBarcode(request.getBarcode())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(prd,purchase.getStore().getId())
                .orElseThrow(() -> new NotFoundException("StoreProduct not found"));
        if(storeProduct.getPrice() != request.getPrice()) {
            StoreProductPriceHistory priceHistory = new StoreProductPriceHistory(storeProduct, storeProduct.getPrice(),storeProduct.getLastUpdated(),LocalDateTime.now());
            storeProduct.setLastUpdated(LocalDateTime.now());
            storeProduct.setPrice(request.getPrice());
            storeProductPriceHistoryRepository.save(priceHistory);
            storeProductRepository.save(storeProduct);
        }

        PurchaseProduct purchaseProduct = new PurchaseProduct(purchase,request.getQuantity(),request.getDiscount(),storeProduct.getProduct().getName(),storeProduct.getPrice());
        purchaseProductRepository.save(purchaseProduct);
        purchase.getProducts().add(purchaseProduct);
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Purchase addNoBarcodeProduct(AddNoBarcodeProductDTO request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        PurchaseProduct purchaseProduct = new PurchaseProduct(
                purchase,
                request.getQuantity(),
                request.getDiscount(),
                request.getProductName(),
                request.getPrice()
        );
        purchase.getProducts().add(purchaseProduct);

        return purchaseRepository.save(purchase);
    }

}
