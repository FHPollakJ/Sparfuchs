package com.sparfuchs.DTO;

import java.time.LocalDateTime;

public record ProductWithPriceDTO(String barcode,
                                  String name,
                                  double price,
                                  Long storeId,
                                  LocalDateTime lastUpdated
){

}
