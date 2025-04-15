package com.sparfuchs.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record PurchaseProductDTO(long purchaseId,
                                 String barcode,
                                 String productName,
                                 int quantity,
                                 int discount,
                                 double price)
{
}

