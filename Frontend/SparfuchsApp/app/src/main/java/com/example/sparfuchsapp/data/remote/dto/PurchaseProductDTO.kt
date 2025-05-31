package com.example.sparfuchsapp.data.remote.dto

import com.example.sparfuchsapp.utils.DiscountType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PurchaseProductDTO(
    val purchaseId: Long,
    val barcode: String? = null,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val discount: Int = 0,
    @Transient val discountThreshold: Int? = null,
    @Transient val productId: Long? = null
)

fun PurchaseProductDTO.calculateTotal(): Double {
    return if (discount > 0) {
        val discountedUnitPrice = price * (1 - discount / 100.0)
        discountedUnitPrice * quantity
    } else {
        price * quantity
    }
}