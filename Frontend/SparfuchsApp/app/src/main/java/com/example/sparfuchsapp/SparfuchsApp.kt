package com.example.sparfuchsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SparfuchsApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}