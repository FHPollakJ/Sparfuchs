package com.sparfuchs.purchase;
import com.sparfuchs.purchaseProduct.PurchaseProduct;
import com.sparfuchs.store.Store;
import com.sparfuchs.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    private Store store;

    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> products = new ArrayList<>();

    private boolean completed = false;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    protected Purchase() {
    }

    public Purchase(User user, Store store, LocalDateTime createdAt) {
        this.user = user;
        this.completed = false;
        this.store = store;
        this.createdAt = createdAt;
    }

    public Store getStore() {
        return store;
    }

    public List<PurchaseProduct> getProducts() {
        return products;
    }

    public Long getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        this.completed = true;
        this.user.updateTotals();
    }

    public User getUser() {
        return user;
    }

    public double getTotalSpent() {
        return products.stream()
                .mapToDouble(PurchaseProduct::getTotalPrice)
                .sum();
    }

    public double getTotalSaved() {
        return products.stream()
                .mapToDouble(PurchaseProduct::getTotalSavings)
                .sum();
    }

}


