package com.sparfuchs.storeProduct;
import com.sparfuchs.product.Product;
import com.sparfuchs.purchase.Purchase;
import com.sparfuchs.store.Store;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_products")
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Store store;

    @Column(nullable = false)
    private double price;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "storeProduct", cascade = CascadeType.ALL, orphanRemoval = true) //oprhan removal ?? lmao
    private List<StoreProductPriceHistory> priceHistories = new ArrayList<>();

    public StoreProduct() {}

    public StoreProduct(Product product, Store store, double price, LocalDateTime lastUpdated) {
        this.product = product;
        this.store = store;
        this.price = price;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public Store getStore() {
        return store;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


