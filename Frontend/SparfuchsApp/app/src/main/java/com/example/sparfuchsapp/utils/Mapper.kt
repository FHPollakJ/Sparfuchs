package com.example.sparfuchsapp.utils

import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductResponseDTO

fun PurchaseProductResponseDTO.toPurchaseProductDTO(purchaseId: Long): PurchaseProductDTO {
    return PurchaseProductDTO(
        purchaseId = purchaseId,
        productId = this.id,
        productName = this.productName,
        quantity = this.quantity,
        discount = this.discount,
        price = this.price,
        barcode = ""
    )
}