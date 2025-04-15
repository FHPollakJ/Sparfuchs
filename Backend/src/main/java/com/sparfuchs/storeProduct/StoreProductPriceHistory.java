package com.sparfuchs.storeProduct;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class StoreProductPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private StoreProduct storeProduct;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;

    public StoreProductPriceHistory(){

    }

    public StoreProductPriceHistory(StoreProduct storeProduct, double price, LocalDateTime timestamp, LocalDateTime endTime) {
        this.storeProduct = storeProduct;
        this.price = price;
        this.startTime = timestamp;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public StoreProduct getStoreProduct() {
        return storeProduct;
    }

    public void setStoreProduct(StoreProduct storeProduct) {
        this.storeProduct = storeProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime timestamp) {
        this.startTime = timestamp;
    }
}
