package com.sparfuchs.DTO;


public record AddProductToPurchaseDTO (
     long purchaseId,
     String barcode,
     int quantity,
     int discount,
     double price)
{
}

