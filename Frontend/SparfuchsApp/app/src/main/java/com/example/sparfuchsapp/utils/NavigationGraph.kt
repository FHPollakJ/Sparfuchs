package com.example.sparfuchsapp.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO
import com.example.sparfuchsapp.ui.screens.AccountScreen
import com.example.sparfuchsapp.ui.screens.MainScreen
import com.example.sparfuchsapp.ui.screens.ProductSearchScreen
import com.example.sparfuchsapp.ui.screens.SettingsScreen
import com.example.sparfuchsapp.ui.screens.registerLogin.AuthScreen
import com.example.sparfuchsapp.ui.screens.registerLogin.AuthViewModel
import com.example.sparfuchsapp.ui.screens.shopping.AddProductScreen
import com.example.sparfuchsapp.ui.screens.shopping.PreShoppingScreen
import com.example.sparfuchsapp.ui.screens.shopping.ScannerScreen
import com.example.sparfuchsapp.ui.screens.shopping.ShoppingScreen
import com.example.sparfuchsapp.ui.screens.shopping.ShoppingViewModel
import okhttp3.internal.wait
import java.time.LocalDateTime

//Navigation graph, tells what routes show what screen
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                navController = navController,
                viewModel = shoppingViewModel,
                padding = innerPadding
            ) }
        composable(Routes.SETTINGS) { SettingsScreen(
            padding = innerPadding
        ) }
        composable(Routes.SHOPPING) {
            val purchase = shoppingViewModel.purchase.collectAsState()

            if (purchase.value != null){ //Waits untill purchase is loaded
                ShoppingScreen(
                    padding = innerPadding,
                    viewModel = shoppingViewModel,
                    navController = navController
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
                LaunchedEffect(purchase.value) {
                    if (purchase.value != null) {
                        navController.navigate(Routes.SHOPPING) {
                            popUpTo(Routes.SHOPPING) { inclusive = true }
                        }
                    }
                }
            }

        }
        composable(Routes.PRODUCT_HISTORY) { ProductSearchScreen(
            padding = innerPadding
        ) }
        composable(
            route = "${Routes.ADD_PRODUCT}/{purchaseId}/{storeId}?scanBarcode={scanBarcode}",
            arguments = listOf(
                navArgument("purchaseId") { type = NavType.LongType },
                navArgument("storeId") { type = NavType.LongType },
                navArgument("scanBarcode") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ){ backStackEntry ->
            val purchaseId = backStackEntry.arguments?.getLong("purchaseId") ?: return@composable
            val barcode = backStackEntry.arguments?.getString("scanBarcode")
            val storeId = backStackEntry.arguments?.getLong("storeId") ?: return@composable

            AddProductScreen(
                purchaseId = purchaseId,
                scanBarcode = barcode?.takeIf { it.isNotBlank() },
                onSave = { product ->
                    shoppingViewModel.createProductAndAddToPurchase(product, storeId)
                    navController.navigate(Routes.SHOPPING)
                },
                onCancel = { navController.popBackStack() },
                padding = innerPadding
            )
        }
        composable(Routes.ACCOUNT) { AccountScreen(
            padding = innerPadding,
            viewModel = authViewModel,
            onLogout = {
                navController.navigate(Routes.AUTH) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
        }
        composable(Routes.AUTH){
            AuthScreen(
                viewModel = authViewModel,
                padding = innerPadding,
                navController = navController
            )
        }
    }
}