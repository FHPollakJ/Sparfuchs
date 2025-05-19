package com.example.sparfuchsapp.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sparfuchsapp.ui.components.BottomNavItem
import com.example.sparfuchsapp.ui.components.TopNavItem
import com.example.sparfuchsapp.ui.screens.AccountScreen
import com.example.sparfuchsapp.ui.screens.AuthScreen
import com.example.sparfuchsapp.ui.screens.MainScreen
import com.example.sparfuchsapp.ui.screens.shopping.PreShoppingScreen
import com.example.sparfuchsapp.ui.screens.ProductSearchScreen
import com.example.sparfuchsapp.ui.screens.ScannerScreen
import com.example.sparfuchsapp.ui.screens.SettingsScreen
import com.example.sparfuchsapp.ui.screens.shopping.ShoppingScreen
import com.example.sparfuchsapp.ui.screens.viewModels.AuthViewModel
import com.example.sparfuchsapp.ui.screens.shopping.ShoppingViewModel

//Navigation graph, tells what routes show what screen
@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String
) {
    val authViewModel: AuthViewModel = viewModel()
    val shoppingViewModel: ShoppingViewModel = viewModel()

    NavHost(navController, startDestination = startDestination) {
        composable(Routes.HOME) { MainScreen(
            viewModel = authViewModel,
            padding = innerPadding
        ) }
        composable(Routes.SCANNER) {
            ScannerScreen(
                onBarcodeScanned = { barcode ->
                    println("Scanned: $barcode")
                },
                onCancel = { false }
            ) }
        composable(Routes.SETTINGS) { SettingsScreen(
            padding = innerPadding
        ) }
        composable(Routes.SHOPPING) {
            val purchase = shoppingViewModel.purchase.collectAsState()

            if (purchase.value != null){
                ShoppingScreen(
                    padding = innerPadding,
                    viewModel = shoppingViewModel
                )
            } else {
                PreShoppingScreen(
                    padding = innerPadding,
                    viewModel = shoppingViewModel,
                    onStartPurchase = {
                        navController.navigate(Routes.SHOPPING) {
                            popUpTo(Routes.SHOPPING) { inclusive = true }
                        }
                    }
                )
            }

        }
        composable(Routes.PRODUCT_SEARCH) { ProductSearchScreen() }
        composable(Routes.ACCOUNT) { AccountScreen(
            padding = innerPadding,
            viewModel = authViewModel
        ) }
        composable(Routes.AUTH){
            AuthScreen(
                viewModel = authViewModel,
                padding = innerPadding,
                navController  = navController
            )
        }
    }
}