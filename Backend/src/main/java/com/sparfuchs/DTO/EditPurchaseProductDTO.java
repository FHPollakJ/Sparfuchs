package com.sparfuchs.DTO;

public record EditPurchaseProductDTO(long id,
                                     long purchaseId,
                                     int quantity,
                                     int discount,
                                     double price
)
{
}
