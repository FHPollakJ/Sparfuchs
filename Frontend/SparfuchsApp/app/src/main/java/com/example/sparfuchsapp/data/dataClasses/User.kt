package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val password: String
)
