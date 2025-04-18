package com.example.sparfuchsapp.data.dataClasses

data class PurchaseProduct(
    val id: Long,
    val product: Product,
    val purchase: Purchase,
    val quantity: Int,
    val total: Double
)
