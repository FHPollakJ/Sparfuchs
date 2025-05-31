package com.example.sparfuchsapp.data.remote.api

import com.example.sparfuchsapp.data.remote.dto.GetProductDTO
import com.example.sparfuchsapp.data.remote.dto.ProductPriceHistoryDTO
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProductApiService {

    @POST("products/getProduct")
    suspend fun getProduct(@Body request: GetProductDTO): Response<ProductWithPriceDTO>

    @POST("products/createProduct")
    suspend fun createProduct(@Body request: ProductWithPriceDTO)

    @POST("products/priceHistory")
    suspend fun getPriceHistory(@Body request: ProductWithPriceDTO): Response<List<ProductPriceHistoryDTO>>
}
