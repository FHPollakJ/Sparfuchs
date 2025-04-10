package com.sparfuchs.product;
import jakarta.persistence.*;

@Entity
public class Product {
    @Column(nullable = false)
    private String name;

    @Id @Column(unique = true)
    private String barcode;
}

