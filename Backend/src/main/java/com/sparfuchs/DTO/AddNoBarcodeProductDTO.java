package com.sparfuchs.DTO;


public class AddNoBarcodeProductDTO {
    private Long purchaseId;
    private String productName;
    private float price;
    private int quantity;
    private int discount;

    public AddNoBarcodeProductDTO() {
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public String getProductName() {
        return productName;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }
}

