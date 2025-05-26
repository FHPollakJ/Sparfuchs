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
import com.example.sparfuchsapp.utils.Routes

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
                    navController.popBackStack()
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Account"
                )
            }
        },
        actions = { //ACCOUNT BUTTON
            IconButton(
                onClick = {
                    navController.navigate(Routes.ACCOUNT){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Back"
                )
            }
        }
    )
}