package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequestDTO(
    val username: String? = null, //Currently Optional for Login
    val email: String,
    val password: String
)
