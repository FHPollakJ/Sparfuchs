package com.sparfuchs.purchase;

import com.sparfuchs.DTO.SaveUnknownProductDTO;
import com.sparfuchs.DTO.AddProductToPurchaseDTO;
import com.sparfuchs.DTO.StartPurchaseDTO;
import com.sparfuchs.storeProduct.StoreProduct;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/start") //purchase dto und storeproductdto und so noch machen
    public ResponseEntity<Purchase> startPurchase(@RequestBody StartPurchaseDTO request, HttpSession session) {
        Purchase purchase = purchaseService.startPurchase(request, (long)session.getAttribute("userId"));
        return ResponseEntity.ok(purchase);
    }

    @PostMapping("/addProduct") //remove product und edit product und addUnknown
    public ResponseEntity<Purchase> addProductPurchase(@RequestBody AddProductToPurchaseDTO request, HttpSession session) {
        Purchase purchase = purchaseService.addProductToPurchase(request);
        return ResponseEntity.ok(purchase);
    }

    //end purchase
    //delete purchase ( nur incompleted )
}