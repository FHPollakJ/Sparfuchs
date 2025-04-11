package com.sparfuchs.DTO;

public class SaveUnknownProductDTO {
    private Long storeId;
    private String productName;
    private String barcode;
    private double price;

    public SaveUnknownProductDTO() {
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public double getPrice() {
        return price;
    }
}

