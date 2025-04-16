package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Purchase(
    val id: Long,
    val user: User,
    val store: Store,
    val createdAt: LocalDateTime,
    val products: List<Product>
)
