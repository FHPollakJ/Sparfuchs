package com.example.sparfuchsapp.data.remote.api

import com.example.sparfuchsapp.data.remote.dto.UserResponseDTO
import com.example.sparfuchsapp.data.remote.dto.AuthRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {

    @POST("users/register")
    suspend fun register(@Body request: AuthRequestDTO): Response<UserResponseDTO>

    @POST("users/login")
    suspend fun login(@Body request: AuthRequestDTO): Response<UserResponseDTO>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>

    @PATCH("users/edit")
    suspend fun editUser(@Body request: AuthRequestDTO): Response<UserResponseDTO>

    @DELETE("users/delete")
    suspend fun deleteUser(): Response<Unit>
}