package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val error: String
)

