package com.example.sparfuchsapp.data.remote

import com.example.sparfuchsapp.data.remote.api.ProductApiService
import com.example.sparfuchsapp.data.remote.api.PurchaseApiService
import com.example.sparfuchsapp.data.remote.api.UserApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
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
            .cookieJar(CookieManager)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    object CookieManager : CookieJar {
        internal val cookieStore = mutableMapOf<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: emptyList()
        }
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val purchaseApi: PurchaseApiService by lazy {
        retrofit.create(PurchaseApiService::class.java)
    }

    val productApi: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

}