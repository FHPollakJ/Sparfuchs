package com.sparfuchs.DTO;

public record SaveUnknownProductDTO (
     Long storeId,
     String productName,
     String barcode,
     double price){
}

