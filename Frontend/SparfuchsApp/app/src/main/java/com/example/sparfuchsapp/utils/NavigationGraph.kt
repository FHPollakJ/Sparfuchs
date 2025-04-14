package com.example.sparfuchsapp.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sparfuchsapp.ui.components.BottomNavItem
import com.example.sparfuchsapp.ui.components.TopNavItem
import com.example.sparfuchsapp.ui.screens.AccountScreen
import com.example.sparfuchsapp.ui.screens.MainScreen
import com.example.sparfuchsapp.ui.screens.ScannerScreen
import com.example.sparfuchsapp.ui.screens.SettingsScreen

//Navigation graph, tells what routes show what screen
@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) { MainScreen(
            padding = innerPadding
        ) }
        composable(BottomNavItem.Scanner.route) {
            ScannerScreen(
            onBarcodeScanned = { barcode ->
                println("Scanned: $barcode")
            }
        ) }
        composable(BottomNavItem.Settings.route) { SettingsScreen() }
        composable(TopNavItem.Account.route) { AccountScreen() }
        composable(TopNavItem.Back.route) { navController.popBackStack() }
    }
}