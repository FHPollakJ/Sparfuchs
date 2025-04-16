package com.example.sparfuchsapp.data.remote

import com.example.sparfuchsapp.data.remote.api.UserApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.getValue

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // for Android emulator localhost

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateTimeJsonAdapter())
            .build()
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    val userApi: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(UserApiService::class.java)
    }
}