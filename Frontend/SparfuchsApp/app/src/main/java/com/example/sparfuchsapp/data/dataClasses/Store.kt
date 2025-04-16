package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Store(
    val id: Long,
    val name: String
)
