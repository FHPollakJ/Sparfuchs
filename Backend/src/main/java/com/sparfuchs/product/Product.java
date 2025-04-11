package com.sparfuchs.product;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String barcode;

    private String name;

    public Product() {}

    public Product(String barcode, String name ) {
        this.barcode = barcode;
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

}


