package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PurchaseProductResponseDTO (
    val id: Long,
    val productName: String,
    val quantity: Int,
    val discount: Int,
    val price: Double
)