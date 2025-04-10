package com.sparfuchs.purchaseProduct;
import com.sparfuchs.purchase.Purchase;
import com.sparfuchs.storeProduct.StoreProduct;
import jakarta.persistence.*;

@Entity
public class PurchaseProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase einkauf;

    @ManyToOne
    @JoinColumn(name = "storeProduct_id", nullable = false)
    private StoreProduct storeProdukt;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double discount;
}

