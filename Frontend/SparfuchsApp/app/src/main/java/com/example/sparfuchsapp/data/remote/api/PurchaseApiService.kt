package com.example.sparfuchsapp.data.remote.api

import com.example.sparfuchsapp.data.remote.dto.EditPurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseIdDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.StartPurchaseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST

interface PurchaseApiService {

    @POST("purchase/startPurchase")
    suspend fun startPurchase(@Body request: StartPurchaseDTO): Response<PurchaseDTO>

    @POST("purchase/addProductToPurchase")
    suspend fun addProductToPurchase(@Body request: PurchaseProductDTO): Response<PurchaseDTO>

    @PATCH("purchase/finishPurchase")
    suspend fun finishPurchase(@Body request: PurchaseIdDTO)

    @PATCH("purchase/editProductInPurchase")
    suspend fun editProductInPurchase(@Body request: EditPurchaseProductDTO)

    @DELETE("purchase/deletePurchase")
    suspend fun deletePurchase(@Body request: PurchaseIdDTO)

    @POST("purchase/removeProductFromPurchase")
    suspend fun removeProductFromPurchase(@Body request: PurchaseProductDTO): Response<PurchaseDTO>
}