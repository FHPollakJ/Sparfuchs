package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditPurchaseProductDTO (
    val id: Long,
    val purchaseId: Long,
    val quantity: Int,
    val discount: Int
)