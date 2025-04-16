package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponseDTO(
    val username: String,
    val email: String,
    val purchases: List<PurchaseDTO>
)