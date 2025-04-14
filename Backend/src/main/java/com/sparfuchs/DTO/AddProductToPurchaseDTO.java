package com.sparfuchs.DTO;


import com.sparfuchs.purchase.Purchase;

public record AddProductToPurchaseDTO (
     long purchaseId,
     String barcode,
     int quantity,
     int discount,
     double price)
{
}

