package com.sparfuchs.DTO;

public record PurchaseProductResponseDTO(
                                         long id,
                                         String productName,
                                         int quantity,
                                         int discount,
                                         double price) {
}
