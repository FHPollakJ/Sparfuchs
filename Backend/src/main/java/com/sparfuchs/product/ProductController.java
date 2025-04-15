package com.sparfuchs.product;

import com.sparfuchs.DTO.GetProductDTO;
import com.sparfuchs.DTO.ProductPriceHistoryDTO;
import com.sparfuchs.DTO.ProductWithPriceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getProduct")
    public ResponseEntity<ProductWithPriceDTO> getProductWithPrice(@RequestBody GetProductDTO request) {
        ProductWithPriceDTO product = productService.getProductWithBarcode(request.barcode(), request.storeId());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductWithPriceDTO request) {
        productService.createNewProduct(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/editProduct")
    public ResponseEntity<?> editProduct(@RequestBody ProductWithPriceDTO request) {
        productService.updateProduct(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/priceHistory")
    public ResponseEntity<List<ProductPriceHistoryDTO>> getPriceHistory(@RequestBody ProductWithPriceDTO request) {
        List<ProductPriceHistoryDTO> history = productService.getPriceHistoryForProduct(request);
        return ResponseEntity.ok(history);
    }
    //compare price history stores

}
