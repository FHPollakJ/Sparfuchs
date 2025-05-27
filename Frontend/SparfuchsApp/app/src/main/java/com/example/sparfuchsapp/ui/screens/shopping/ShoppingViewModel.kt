package com.example.sparfuchsapp.ui.screens.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparfuchsapp.data.remote.RetrofitClient
import com.example.sparfuchsapp.data.remote.dto.GetProductDTO
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseIdDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.StartPurchaseDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ShoppingViewModel : ViewModel() {
    private val _purchase = MutableStateFlow<PurchaseDTO?>(null)
    val purchase: StateFlow<PurchaseDTO?> get() = _purchase

    private val _productList = MutableStateFlow<List<PurchaseProductDTO>>(emptyList())
    val productList = _productList.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun startPurchase(storeId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.purchaseApi.startPurchase(
                    StartPurchaseDTO(
                        storeId = storeId,
                        createdAt = LocalDateTime.now()
                    )
                )
                if (response.isSuccessful) {
                    _purchase.value = response.body()
                    _error.value = null
                    Log.d("StartPurchaseCookies", RetrofitClient.CookieManager.cookieStore.toString())
                } else {
                    _error.value = "Start purchase failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    //Local
    fun addProductToPurchase(product: PurchaseProductDTO) {
        viewModelScope.launch {
            _purchase.value?.let { currentPurchase ->
                try {
                    val response = RetrofitClient.purchaseApi.addProductToPurchase(
                        PurchaseProductDTO(
                            purchaseId = currentPurchase.purchaseId,
                            barcode = product.barcode,
                            productName = product.productName,
                            quantity = product.quantity,
                            discount = product.discount,
                            price = product.price
                        )
                    )
                    if (response.isSuccessful) {
                        _purchase.value = response.body()
                        _error.value = null
                    } else {
                        _error.value = "Add product to purchase failed: ${response.code()}"
                    }
                } catch (e: Exception){
                    _error.value = "Network error: ${e.message}"
                }
            }
        }
    }

    fun removeProductFromPurchase(product: PurchaseProductDTO) {
        viewModelScope.launch {
            _purchase.value?.let{ currentPurchase ->
                try {
                    val response = RetrofitClient.purchaseApi.removeProductFromPurchase(
                        PurchaseProductDTO(
                            purchaseId = currentPurchase.purchaseId,
                            barcode = product.barcode,
                            productName = product.productName,
                            quantity = product.quantity,
                            discount = product.discount,
                            price = product.price
                        )
                    )
                    if (response.isSuccessful){
                        _purchase.value = response.body()
                        _error.value = null
                    } else {
                        _error.value = "Remove product from purchase failed: ${response.code()}"
                    }
                } catch (e: Exception) {
                    _error.value = "Network error: ${e.message}"
                }
            }
        }
    }

    fun getProduct(barcode: String, onResult: (ProductWithPriceDTO?) -> Unit) {
        viewModelScope.launch {
            try {
                val request =
                    GetProductDTO(barcode = barcode, storeId = _purchase.value?.storeId ?: 0)
                val response = RetrofitClient.productApi.getProduct(request)
                if (response.isSuccessful) {
                    val product = response.body()
                    _error.value = null
                    onResult(product)
                } else { //No Product found
                    _error.value = "Get product failed: ${response.code()}"
                    onResult(null)
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
                onResult(null)
            }
        }
    }

    fun createProduct(barcode: String, product: ProductWithPriceDTO) {
        viewModelScope.launch {
            try {
                RetrofitClient.productApi.createProduct(
                    ProductWithPriceDTO(
                        name = product.name,
                        barcode = barcode,
                        price = product.price,
                        storeId = product.storeId,
                        lastUpdated = LocalDateTime.now()
                    )
                )
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun finishPurchase(purchaseId: Long) {
        viewModelScope.launch {
            try {
                RetrofitClient.purchaseApi.finishPurchase(PurchaseIdDTO(purchaseId))
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun editProductInPurchase(product: PurchaseProductDTO) {
        viewModelScope.launch {
            try {
                RetrofitClient.purchaseApi.editProductInPurchase(product)
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearShoppingList(){
        _purchase.value = null
    }
}