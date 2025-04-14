package com.sparfuchs.DTO;

import com.sparfuchs.store.Store;

import java.time.LocalDateTime;

public record ProductWithPriceResponseDTO ( String barcode,
         String name,
         double price,
         Store store,
         LocalDateTime lastUpdated
){

}
