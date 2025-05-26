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
    @Transient val discountType: DiscountType = DiscountType.NONE,
    val discount: Int = 0,
    @Transient val discountThreshold: Int? = null
)

fun PurchaseProductDTO.calculateTotal(): Double {
    val basePrice = quantity * price
    return when (discountType) {
        DiscountType.NONE -> basePrice

        DiscountType.PERCENTAGE_OFF_ONE_ITEM -> {
            if (quantity > 0) {
                val discountedItem = price * (1 - discount / 100.0)
                discountedItem + (quantity - 1) * price
            } else 0.0
        }

        DiscountType.PERCENTAGE_OFF_ALL_ITEMS -> {
            basePrice * (1 - discount / 100.0)
        }

        DiscountType.BUY_X_GET_Y_FREE -> {
            val x = discountThreshold ?: return basePrice
            val y = discount.toInt()
            val setSize = x + y

            val fullSets = quantity / setSize
            val leftovers = quantity % setSize

            (fullSets * x + leftovers.coerceAtMost(x)) * price
        }

        DiscountType.BUY_X_PAY_FOR_Y -> {
            val x = discountThreshold ?: return basePrice
            val y = discount.toInt()
            val fullSets = quantity / x
            val leftovers = quantity % x

            (fullSets * y + leftovers) * price
        }

        DiscountType.FIXED_AMOUNT_OFF_ONE_ITEM -> {
            if (quantity > 0) {
                val discounted = (price - discount).coerceAtLeast(0.0)
                discounted + (quantity - 1) * price
            } else 0.0
        }

        DiscountType.FIXED_AMOUNT_OFF_TOTAL -> {
            (basePrice - discount).coerceAtLeast(0.0)
        }
    }
}