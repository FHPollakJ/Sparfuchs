package com.example.sparfuchsapp.utils

import android.util.Log
import com.example.sparfuchsapp.data.dataClasses.ErrorResponse
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

fun translateErrorMessage(errorBody: ResponseBody?): String {
    return try {
        val raw = errorBody?.string()
        Log.e("LoginErrorRaw", raw ?: "null")

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        val parsed = adapter.fromJson(raw ?: "")
        parsed?.error ?: "Unknown error"
    } catch (e: Exception) {
        Log.e("LoginErrorParse", e.toString())
        "Unexpected error"
    }
}