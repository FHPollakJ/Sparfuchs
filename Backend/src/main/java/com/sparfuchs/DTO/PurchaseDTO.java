package com.sparfuchs.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseDTO(Long purchaseId,
                          Long storeId,
                          LocalDateTime lastUpdated,
                          List<PurchaseProductResponseDTO> products,
                          boolean isCompleted ,
                          double totalSpent,
                          double totalSaved) {
}
