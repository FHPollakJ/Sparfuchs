package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class PurchaseDTO (
    val purchaseId: Long,
    val storeId: Long,
    val lastUpdated: LocalDateTime,
    val products: List<PurchaseProductResponseDTO>,
    val isCompleted: Boolean,
    val totalSpent: Double,
    val totalSaved: Double
)