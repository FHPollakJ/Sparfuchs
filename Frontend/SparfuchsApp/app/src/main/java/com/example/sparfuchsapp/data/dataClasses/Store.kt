package com.example.sparfuchsapp.data.dataClasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Store(
    val id: Long,
    val name: String,
    val logoResId: Int? = null
)

object StoreRepository {
    val storeMap: Map<Long, Store> = mapOf(
        1L to Store(1, "Billa", ),
        2L to Store(2, "Spar", )
    )

    fun getStore(storeId: Long): Store =
        storeMap[storeId] ?: throw IllegalArgumentException("Invalid store ID")
}