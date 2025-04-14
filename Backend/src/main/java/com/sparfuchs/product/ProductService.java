package com.sparfuchs.product;

import com.sparfuchs.DTO.ProductWithPriceResponseDTO;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.purchase.Purchase;
import com.sparfuchs.purchase.PurchaseRepository;
import com.sparfuchs.purchaseProduct.PurchaseProduct;
import com.sparfuchs.purchaseProduct.PurchaseProductRepository;
import com.sparfuchs.store.Store;
import com.sparfuchs.store.StoreRepository;
import com.sparfuchs.storeProduct.StoreProduct;
import com.sparfuchs.storeProduct.StoreProductPriceHistory;
import com.sparfuchs.storeProduct.StoreProductPriceHistoryRepository;
import com.sparfuchs.storeProduct.StoreProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final StoreProductPriceHistoryRepository storeProductPriceHistoryRepository;
    private final StoreRepository storeRepository;

    public ProductService(
            ProductRepository productRepository,
            StoreProductRepository storeProductRepository,
            StoreProductPriceHistoryRepository storeProductPriceHistoryRepository,
            StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
        this.storeProductPriceHistoryRepository = storeProductPriceHistoryRepository;
        this.storeRepository = storeRepository;
    }

    public ProductWithPriceResponseDTO getProductWithBarcode(String barcode, Long storeId) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Product with barcode " + barcode + " not found."));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product, storeId)
                .orElseThrow(() -> new NotFoundException("Product not available in the selected store."));

        return new ProductWithPriceResponseDTO(
                product.getBarcode(),
                product.getName(),
                storeProduct.getPrice(),
                storeProduct.getStore(),
                storeProduct.getLastUpdated()
        );
    }

    public StoreProduct createNewProduct(String barcode, String productName, Long storeId, double price) {
        if (productRepository.findByBarcode(barcode).isPresent()) {
            throw new RuntimeException("A product with this barcode already exists.");
        }

        Product product = new Product(barcode, productName);
        product = productRepository.save(product);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        StoreProduct storeProduct = new StoreProduct(product, store, price, LocalDateTime.now());
        return storeProductRepository.save(storeProduct);
    }

    public void updatePrice(StoreProduct storeProduct, double newPrice) {
        if (storeProduct.getPrice() != newPrice) {
            StoreProductPriceHistory priceHistory = new StoreProductPriceHistory(
                    storeProduct,
                    storeProduct.getPrice(),
                    storeProduct.getLastUpdated(),
                    LocalDateTime.now()
            );
            storeProduct.setPrice(newPrice);
            storeProduct.setLastUpdated(LocalDateTime.now());
            storeProductPriceHistoryRepository.save(priceHistory);
            storeProductRepository.save(storeProduct);
        }
    }

    public PurchaseProduct createPurchaseProduct(Purchase purchase, int quantity, int discount, String productName, double price) {
        return new PurchaseProduct(purchase, quantity, discount, productName, price);
    }
}


