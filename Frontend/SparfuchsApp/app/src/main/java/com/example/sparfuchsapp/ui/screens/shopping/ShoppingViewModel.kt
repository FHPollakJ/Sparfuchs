package com.example.sparfuchsapp.ui.screens.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparfuchsapp.data.remote.RetrofitClient
import com.example.sparfuchsapp.data.remote.dto.EditPurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.GetProductDTO
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseIdDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductResponseDTO
import com.example.sparfuchsapp.data.remote.dto.StartPurchaseDTO
import com.example.sparfuchsapp.utils.translateErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ShoppingViewModel : ViewModel() {
    private val _purchase = MutableStateFlow<PurchaseDTO?>(null)
    val purchase: StateFlow<PurchaseDTO?> get() = _purchase

    private val _productList = MutableStateFlow<List<PurchaseProductResponseDTO>>(emptyList())
    val productList: StateFlow<List<PurchaseProductResponseDTO>> = _productList

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
                    _error.value = "Start purchase failed: ${translateErrorMessage(response.errorBody())}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

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
                        _error.value = "Add product to purchase failed: ${translateErrorMessage(response.errorBody())}"
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
                        _error.value = "Remove product from purchase failed: ${translateErrorMessage(response.errorBody())}"
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
                    _error.value = "Get product failed: ${translateErrorMessage(response.errorBody())}"
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
                RetrofitClient.userApi.getPurchases()
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun getPurchase(purchaseId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.purchaseApi.getPurchase(PurchaseIdDTO(purchaseId))
                if (response.isSuccessful) {
                    _purchase.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Get purchase failed: ${translateErrorMessage(response.errorBody())}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun editProductInPurchase(product: EditPurchaseProductDTO) {
        viewModelScope.launch {
            try {
                RetrofitClient.purchaseApi.editProductInPurchase(product)
                _purchase.update { current ->
                    current?.copy(
                        products = current.products.map {
                            if (it.id == product.id) {
                                it.copy(
                                    price = product.price,
                                    quantity = product.quantity,
                                    discount = product.discount
                                )
                            } else it
                        }
                    )
                }
                RetrofitClient.purchaseApi.getPurchase(PurchaseIdDTO(product.purchaseId))
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun createProductAndAddToPurchase(product: PurchaseProductDTO, storeId: Long) {
        viewModelScope.launch {
            try {
                if (!product.barcode.isNullOrBlank() && product.barcode != "NOBARCODE") {
                    RetrofitClient.productApi.createProduct(
                        ProductWithPriceDTO(
                            name = product.productName,
                            barcode = product.barcode,
                            price = product.price,
                            storeId = storeId,
                            lastUpdated = LocalDateTime.now()
                        )
                    )
                }

                _purchase.value?.let { currentPurchase ->
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
                        _error.value = "Add product to purchase failed: ${translateErrorMessage(response.errorBody())}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearShoppingList(){
        _purchase.value = null
    }
}