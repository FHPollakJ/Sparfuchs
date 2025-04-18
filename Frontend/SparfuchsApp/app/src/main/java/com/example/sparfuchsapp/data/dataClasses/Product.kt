package com.example.sparfuchsapp.data.dataClasses

data class Product(
    val barcode: String,
    val name: String,
    val price: Double,
    var quantity: Int = 1
) {
    val total: Double
        get() = price * quantity
}

