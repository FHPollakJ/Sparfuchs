package com.sparfuchs.DTO;

public record PurchaseProductDTO(long purchaseId,
                                 String barcode,
                                 String productName,
                                 int quantity,
                                 int discount,
                                 double price)
{
}

