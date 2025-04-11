package com.sparfuchs.product;

import com.sparfuchs.DTO.GetProductDTO;
import com.sparfuchs.DTO.ProductWithPriceResponseDTO;
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
        ProductWithPriceResponseDTO product = productService.getProductWithBarcode(request.getBarcode(), request.getStoreId());
        return ResponseEntity.ok(product);
    }
}
