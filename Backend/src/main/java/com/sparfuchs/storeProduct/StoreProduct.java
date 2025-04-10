package com.sparfuchs.storeProduct;
import com.sparfuchs.product.Product;
import com.sparfuchs.purchase.Purchase;
import com.sparfuchs.store.Store;
import jakarta.persistence.*;

@Entity
public class StoreProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product produkt;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Double price;
}

