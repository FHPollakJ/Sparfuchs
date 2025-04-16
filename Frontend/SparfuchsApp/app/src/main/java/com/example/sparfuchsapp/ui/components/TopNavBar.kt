package com.example.sparfuchsapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopNavBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text("Sparfuchs")
        },
        navigationIcon = { //BACK BUTTON
            IconButton(
                onClick = {
                    navController.navigate(TopNavItem.Back.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    TopNavItem.Back.icon,
                    contentDescription = "Account"
                )
            }
        },
        actions = { //ACCOUNT BUTTON
            IconButton(
                onClick = {
                    navController.navigate(TopNavItem.Account.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    TopNavItem.Account.icon,
                    contentDescription = "Back"
                )
            }
        }
    )
}

sealed class TopNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Account : BottomNavItem("account", Icons.Default.AccountCircle, "Account")
    object Back : BottomNavItem("back", Icons.AutoMirrored.Filled.ArrowBack, "Back")
}