package com.sparfuchs.DTO;

import com.sparfuchs.store.Store;

import java.time.LocalDateTime;

public record ProductWithPriceDTO(String barcode,
                                  String name,
                                  double price,
                                  Long storeId,
                                  LocalDateTime lastUpdated
){

}
