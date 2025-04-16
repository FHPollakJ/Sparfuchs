package com.example.sparfuchsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sparfuchsapp.ui.SparfuchsAppUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("logged_in", false)

        setContent {
            SparfuchsAppUI(startDestination = if (isLoggedIn) "home" else "auth")
        }
    }
}