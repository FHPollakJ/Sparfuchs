package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class StartPurchaseDTO(
    val storeId: Long,
    val createdAt: LocalDateTime
)