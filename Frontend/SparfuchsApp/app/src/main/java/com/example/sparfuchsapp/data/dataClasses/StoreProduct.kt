package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class StoreProduct(
    val id: Long,
    val product: Product,
    val store: Store,
    val price: Double,
    val lastUpdated: LocalDateTime
)
