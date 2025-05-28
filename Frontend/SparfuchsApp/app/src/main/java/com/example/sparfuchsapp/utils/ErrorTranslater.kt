package com.example.sparfuchsapp.utils

import com.example.sparfuchsapp.data.dataClasses.ErrorResponse
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

fun translateErrorMessage(errorBody: ResponseBody?): String {
    return try {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        val parsed = adapter.fromJson(errorBody?.string() ?: "")
        parsed?.message ?: "Unknown error"
    } catch (e: Exception) {
        "Unexpected error"
    }
}