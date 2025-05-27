package com.sparfuchs.purchase;

import com.sparfuchs.DTO.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/startPurchase")
    public ResponseEntity<PurchaseDTO> startPurchase(@RequestBody StartPurchaseDTO request, HttpSession session) {
        PurchaseDTO purchase = purchaseService.startPurchase(request, (long)session.getAttribute("userId"));
        return ResponseEntity.ok(purchase);
    }

    @PostMapping("/addProductToPurchase")
    public ResponseEntity<PurchaseDTO> addProductToPurchase(@RequestBody PurchaseProductDTO request, HttpSession session) {
        PurchaseDTO purchase = purchaseService.addProductToPurchase(request, (long)session.getAttribute("userId"));
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/editProductInPurchase")
    public ResponseEntity<PurchaseDTO> editProductInPurchase(@RequestBody EditPurchaseProductDTO request, HttpSession session) {
        PurchaseDTO purchase = purchaseService.editProductInPurchase(request, (long)session.getAttribute("userId"));
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/finishPurchase")
    public ResponseEntity<Void> finishPurchase(@RequestBody PurchaseIdDTO request, HttpSession session) {
        purchaseService.finishPurchase(request.purchaseId(), (long) session.getAttribute("userId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deletePurchase")
    public ResponseEntity<Void> deletePurchase(@RequestBody PurchaseIdDTO request, HttpSession session) {
        purchaseService.deleteIncompletePurchase(request.purchaseId(), (long) session.getAttribute("userId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeProductFromPurchase")
    public ResponseEntity<PurchaseDTO> removeProductFromPurchase(@RequestBody PurchaseProductDTO request, HttpSession session) {
        PurchaseDTO purchase = purchaseService.removeProductFromPurchase(request, (long) session.getAttribute("userId"));
        return ResponseEntity.ok(purchase);
    }
}