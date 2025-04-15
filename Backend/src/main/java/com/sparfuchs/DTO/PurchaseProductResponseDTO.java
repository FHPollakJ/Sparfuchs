package com.sparfuchs.DTO;

public record PurchaseProductResponseDTO(
                                         String productName,
                                         int quantity,
                                         int discount,
                                         double price) {
}
