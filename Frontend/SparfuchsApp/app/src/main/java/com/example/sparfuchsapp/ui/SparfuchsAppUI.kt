package com.example.sparfuchsapp.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.sparfuchsapp.ui.components.BottomNavBar
import com.example.sparfuchsapp.utils.NavigationGraph
import com.example.sparfuchsapp.ui.components.TopNavBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SparfuchsAppUI(startDestination: String) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopNavBar(navController) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            innerPadding = innerPadding,
            startDestination = startDestination
        )
    }
}