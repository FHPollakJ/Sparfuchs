package com.sparfuchs.product;

import com.sparfuchs.DTO.CreateProductDTO;
import com.sparfuchs.DTO.GetProductDTO;
import com.sparfuchs.DTO.ProductWithPriceResponseDTO;
import com.sparfuchs.storeProduct.StoreProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/get")
    public ResponseEntity<ProductWithPriceResponseDTO> getProductWithPrice(@RequestBody GetProductDTO request) {
        ProductWithPriceResponseDTO product = productService.getProductWithBarcode(request.barcode(), request.storeId());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/createProduct")
    public ResponseEntity<StoreProduct> createProduct(@RequestBody CreateProductDTO request) {
        StoreProduct storeProduct = productService.createNewProduct(
                request.barcode(),
                request.productName(),
                request.storeId(),
                request.price()
        );
        return ResponseEntity.ok(storeProduct);
    }
}
