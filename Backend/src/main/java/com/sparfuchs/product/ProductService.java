package com.sparfuchs.product;

import com.sparfuchs.DTO.ProductPriceHistoryDTO;
import com.sparfuchs.DTO.ProductWithPriceDTO;
import com.sparfuchs.exception.BadRequestException;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.purchase.Purchase;
import com.sparfuchs.purchaseProduct.PurchaseProduct;
import com.sparfuchs.store.Store;
import com.sparfuchs.store.StoreRepository;
import com.sparfuchs.storeProduct.StoreProduct;
import com.sparfuchs.storeProduct.StoreProductPriceHistory;
import com.sparfuchs.storeProduct.StoreProductPriceHistoryRepository;
import com.sparfuchs.storeProduct.StoreProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public ProductWithPriceDTO getProductWithBarcode(String barcode, Long storeId) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Product with barcode " + barcode + " not found."));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product, storeId)
                .orElseThrow(() -> new NotFoundException("Product not available in the selected store."));

        return new ProductWithPriceDTO(
                product.getBarcode(),
                product.getName(),
                storeProduct.getPrice(),
                storeProduct.getStore().getId(),
                storeProduct.getLastUpdated()
        );
    }
    @Transactional
    public void createNewProduct(ProductWithPriceDTO request) {
        if (productRepository.findByBarcode(request.barcode()).isPresent()) {
            throw new BadRequestException("A product with this barcode already exists.");
        }

        Product product = new Product(request.barcode(), request.name());
        product = productRepository.save(product);

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new NotFoundException("Store not found."));

        StoreProduct storeProduct = new StoreProduct(product, store, request.price(), request.lastUpdated());
        storeProductRepository.save(storeProduct);
    }

    @Transactional
    public void updateProduct(ProductWithPriceDTO request) {
        Product product = productRepository.findByBarcode(request.barcode())
                .orElseThrow(() -> new NotFoundException("Product not found."));
        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product,request.storeId())
                .orElseThrow(() -> new NotFoundException("Product not found in this store"));
        if (storeProduct.getPrice() != request.price()) {
                StoreProductPriceHistory priceHistory = new StoreProductPriceHistory(
                       storeProduct,
                       storeProduct.getPrice(),
                       storeProduct.getLastUpdated(),
                      LocalDateTime.now()
                );
                storeProduct.setPrice(request.price());
                storeProduct.setLastUpdated(LocalDateTime.now());
                storeProductPriceHistoryRepository.save(priceHistory);
        }
        if(!Objects.equals(product.getName(), request.name())) {
            product.setName(request.name());
        }

    }

    public List<ProductPriceHistoryDTO> getPriceHistoryForProduct(ProductWithPriceDTO request) {
        Product product = productRepository.findByBarcode(request.barcode())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        StoreProduct storeProduct = storeProductRepository.findByProductAndStoreId(product, request.storeId())
                .orElseThrow(() -> new NotFoundException("StoreProduct not found"));

        List<StoreProductPriceHistory> historyList = storeProductPriceHistoryRepository.findByStoreProduct(storeProduct)
                .orElseThrow(() -> new NotFoundException("Product has no different prices"));

        return historyList.stream()
                .map(h -> new ProductPriceHistoryDTO(h.getPrice(), h.getStartTime(), h.getEndTime()))
                .collect(Collectors.toList());
    }


    public PurchaseProduct createPurchaseProduct(Purchase purchase,Product product, int quantity, int discount, String productName, double price) {
        return new PurchaseProduct(purchase,product, quantity, discount, productName, price);
    }

    public PurchaseProduct createPurchaseProductWithoutBarcode(Purchase purchase, int quantity, int discount, String productName, double price) {
        return new PurchaseProduct(purchase, quantity, discount, productName, price);
    }
}


