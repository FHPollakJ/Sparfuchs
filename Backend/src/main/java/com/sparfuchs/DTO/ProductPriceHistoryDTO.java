package com.sparfuchs.DTO;

import java.time.LocalDateTime;

public record ProductPriceHistoryDTO(double price, LocalDateTime startTime, LocalDateTime endTime) {
}

