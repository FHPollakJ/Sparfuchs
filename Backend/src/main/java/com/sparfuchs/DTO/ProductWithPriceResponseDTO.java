package com.sparfuchs.DTO;

import com.sparfuchs.store.Store;

import java.time.LocalDateTime;

public class ProductWithPriceResponseDTO {
    private String barcode;
    private String name;
    private double price;
    private Store store;
    private LocalDateTime lastUpdated;

    public ProductWithPriceResponseDTO(String barcode, String name, double price, Store store, LocalDateTime lastUpdated) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.store = store;
        this.lastUpdated = lastUpdated;
    }
    public ProductWithPriceResponseDTO(){

    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
