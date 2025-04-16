package com.example.sparfuchsapp.ui.screens.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.sparfuchsapp.data.dataClasses.Product

class ShoppingViewModel : ViewModel() {
    val productList = mutableStateListOf<Product>()

    fun addProduct(product: Product) {
        val existing = productList.find { it.barcode == product.barcode }
        if (existing != null) {
            val index = productList.indexOf(existing)
            productList[index] = existing.copy(amount = existing.amount + 1)
        } else {
            productList.add(product)
        }
    }

    fun removeProduct(product: Product) {
        productList.remove(product)
    }

    fun updateProduct(product: Product, newAmount: Int){
        productList.indexOf(product).takeIf { it != -1 }?.let { index ->
            val updated = product.copy(amount = newAmount)
            productList[index] = updated
        }
    }

    fun clearShoppingList(){
        productList.clear()
    }
}