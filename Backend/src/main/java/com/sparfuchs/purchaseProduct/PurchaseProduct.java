package com.sparfuchs.purchaseProduct;

import com.sparfuchs.product.Product;
import com.sparfuchs.purchase.Purchase;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_products")
public class PurchaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = true)
    private Product product;
    @ManyToOne(optional = false)
    private Purchase purchase;
    private double price = 0;
    private int discountPercent = 0;
    private int quantity = 0;
    private String name;

    protected PurchaseProduct() {}

    public PurchaseProduct(Purchase purchase, Product product, int quantity, int discountPercentage, String name, double price) {
        this.purchase = purchase;
        this.product = product;
        this.quantity = quantity;
        this.discountPercent = discountPercentage;
        this.price = price;
        this.name = name;
    }
    public PurchaseProduct(Purchase purchase, int quantity, int discountPercentage, String name, double price) {
        this.purchase = purchase;
        this.quantity = quantity;
        this.discountPercent = discountPercentage;
        this.price = price;
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double getTotalPrice() {
        double originalPrice = this.getPrice();
        double discountedPrice = originalPrice * (1 - (double) discountPercent / 100);
        return discountedPrice * quantity;
    }

    public double getTotalSavings() {
        double originalPrice = this.getPrice();
        double savingsPerItem = originalPrice * ((double) discountPercent / 100);
        return savingsPerItem * quantity;
    }

    public Product getProduct() {
        return product;
    }
}


