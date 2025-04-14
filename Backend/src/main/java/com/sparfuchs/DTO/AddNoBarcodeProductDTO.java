package com.sparfuchs.DTO;


public record AddNoBarcodeProductDTO(Long purchaseId,
        String productName,
        float price,
        int quantity,
        int discount) {
}

