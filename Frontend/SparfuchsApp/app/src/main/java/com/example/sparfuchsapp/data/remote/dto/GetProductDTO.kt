package com.example.sparfuchsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetProductDTO (
    val barcode: String,
    val storeId: Long
)