package com.sparfuchs.DTO;


import com.sparfuchs.purchase.Purchase;

public class AddProductToPurchaseDTO {
    private long purchaseId;
    private String barcode;
    private int quantity;
    private int discount;
    private double price;

    public AddProductToPurchaseDTO() {
    }

    public long getPurchaseId() {
        return purchaseId;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public double getPrice() {
        return price;
    }
}

