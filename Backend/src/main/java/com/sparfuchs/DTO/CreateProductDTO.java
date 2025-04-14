package com.sparfuchs.DTO;

public record CreateProductDTO
     (String barcode,
     String productName,
     Long storeId,
     double price)
{
}

