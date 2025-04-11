package com.sparfuchs.product;

import com.sparfuchs.DTO.ProductWithPriceResponseDTO;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.purchase.PurchaseRepository;
import com.sparfuchs.purchaseProduct.PurchaseProductRepository;
import com.sparfuchs.storeProduct.StoreProduct;
import com.sparfuchs.storeProduct.StoreProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;

    public ProductService(
            ProductRepository productRepository,
            StoreProductRepository storeProductRepository,
            PurchaseRepository purchaseRepository,
            PurchaseProductRepository purchaseProductRepository
    ) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseProductRepository = purchaseProductRepository;
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
}

