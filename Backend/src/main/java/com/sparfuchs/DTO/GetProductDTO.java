package com.sparfuchs.DTO;

public class GetProductDTO {
    private String barcode;
    private Long storeId;

    public GetProductDTO() {
    }

    public String getBarcode() {
        return barcode;
    }

    public Long getStoreId() {
        return storeId;
    }
}

