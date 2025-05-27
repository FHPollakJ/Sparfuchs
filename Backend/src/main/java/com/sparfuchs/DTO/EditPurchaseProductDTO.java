package com.sparfuchs.DTO;

public record EditPurchaseProductDTO(long purchaseId,
                                     String barcode,
                                     String productName,
                                     int oldQuantity,
                                     int oldDiscount,
                                     double oldPrice,
                                     int quantity,
                                     int discount,
                                     double price)
{
}
