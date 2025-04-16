package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PurchaseIdDTO (
    val purchaseId: String
)