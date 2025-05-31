package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ProductPriceHistoryDTO(
    val price: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
