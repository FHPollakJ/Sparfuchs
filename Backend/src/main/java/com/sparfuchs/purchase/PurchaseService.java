package com.sparfuchs.purchase;

import com.sparfuchs.DTO.AddNoBarcodeProductDTO;
import com.sparfuchs.DTO.SaveUnknownProductDTO;
import com.sparfuchs.DTO.AddProductToPurchaseDTO;
import com.sparfuchs.DTO.StartPurchaseDTO;
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

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StoreProductRepository storeProductRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            StoreProductRepository storeProductRepository,
            PurchaseProductRepository purchaseProductRepository,
            UserRepository userRepository,
            ProductService productService
    ) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.storeProductRepository = storeProductRepository;
        this.purchaseProductRepository = purchaseProductRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    public Purchase startPurchase(StartPurchaseDTO request, long userId) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new NotFoundException("Store not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Purchase purchase = new Purchase(user, store, LocalDateTime.now());
        return purchaseRepository.save(purchase);
    }

    public Purchase addProductToPurchase(AddProductToPurchaseDTO request) {
        //check if completed
        Purchase purchase = purchaseRepository.findById(request.purchaseId())
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        Product product = productRepository.findByBarcode(request.barcode())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product, purchase.getStore().getId())
                .orElseThrow(() -> new NotFoundException("StoreProduct not found"));

        productService.updatePrice(storeProduct, request.price());

        PurchaseProduct purchaseProduct = productService.createPurchaseProduct(
                purchase,
                request.quantity(),
                request.discount(),
                storeProduct.getProduct().getName(),
                storeProduct.getPrice()
        );

        purchaseProductRepository.save(purchaseProduct);
        purchase.getProducts().add(purchaseProduct);
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Purchase addNoBarcodeProduct(AddNoBarcodeProductDTO request) {
        Purchase purchase = purchaseRepository.findById(request.purchaseId())
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        PurchaseProduct purchaseProduct = productService.createPurchaseProduct(
                purchase,
                request.quantity(),
                request.discount(),
                request.productName(),
                request.price()
        );

        purchaseProductRepository.save(purchaseProduct);
        purchase.getProducts().add(purchaseProduct);
        return purchaseRepository.save(purchase);
    }
}
