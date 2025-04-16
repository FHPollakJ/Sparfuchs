package com.example.sparfuchsapp.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeJsonAdapter {
    // Serialize LocalDateTime to String
    @ToJson
    fun toJson(dateTime: LocalDateTime): String {
        return dateTime.format(FORMATTER)
    }

    // Deserialize String back to LocalDateTime
    @FromJson
    fun fromJson(dateTime: String?): LocalDateTime {
        return LocalDateTime.parse(dateTime, FORMATTER)
    }

    companion object {
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    }
}
