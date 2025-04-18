package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ProductWithPriceDTO (
    val barcode: String,
    val name: String,
    val price: Double,
    val storeId: Long,
    val lastUpdated: LocalDateTime
)