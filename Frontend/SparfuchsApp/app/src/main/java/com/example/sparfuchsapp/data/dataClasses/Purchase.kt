package com.example.sparfuchsapp.data.dataClasses

import java.time.LocalDateTime

data class Purchase(
    val id: Long,
    val user: User,
    val store: Store,
    val createdAt: LocalDateTime,
    val products: List<Product>
)
