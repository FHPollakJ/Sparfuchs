package com.example.sparfuchsapp.data.repository

import com.example.sparfuchsapp.data.dataClasses.Store
import com.example.sparfuchsapp.data.dataClasses.StoreRepository
import com.example.sparfuchsapp.data.remote.RetrofitClient
import com.example.sparfuchsapp.data.remote.dto.GetProductDTO
import com.example.sparfuchsapp.data.remote.dto.ProductPriceHistoryDTO
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO

class ProductRepository {

    suspend fun getProduct(getProductDTO: GetProductDTO): ProductWithPriceDTO? {
        val response = RetrofitClient.productApi.getProduct(getProductDTO)
        if (response.isSuccessful) return response.body()
        return null
    }

    suspend fun getProductFromStores(barcode: String, storeIds: List<Long>): Map<Store, ProductWithPriceDTO?> {
        val results = mutableMapOf<Store, ProductWithPriceDTO?>()
        for (storeId in storeIds) {
            val product = getProduct(GetProductDTO(barcode, storeId))
            val store = StoreRepository.getStore(storeId)
            results[store] = product
        }
        return results
    }

    suspend fun getPriceHistory(productWithPriceDTO: ProductWithPriceDTO): List<ProductPriceHistoryDTO> {
        val response = RetrofitClient.productApi.getPriceHistory(productWithPriceDTO)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    // Fetch price histories from multiple stores
    suspend fun getPriceHistoriesFromStores(
        product: ProductWithPriceDTO,
        storeIds: List<Long>
    ): Map<Store, List<ProductPriceHistoryDTO>> {
        val result = mutableMapOf<Store, List<ProductPriceHistoryDTO>>()
        for (storeId in storeIds) {
            val store = StoreRepository.getStore(storeId)
            val productForStore = product.copy(storeId = storeId)
            val history = getPriceHistory(productForStore)
            result[store] = history
        }
        return result
    }
}