package com.example.sparfuchsapp.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sparfuchsapp.ui.components.BottomNavBar
import com.example.sparfuchsapp.utils.NavigationGraph
import com.example.sparfuchsapp.ui.components.TopNavBar
import com.example.sparfuchsapp.utils.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SparfuchsAppUI(startDestination: String) {
    val navController = rememberNavController()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val hideBars = currentDestination == Routes.AUTH || currentDestination == Routes.SCANNER

    Scaffold(
        topBar = {
            if(!hideBars) {
                TopNavBar(navController)
            }
        },
        bottomBar = {
            if(!hideBars) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            innerPadding = innerPadding,
            startDestination = startDestination
        )
    }
}