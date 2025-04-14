package com.example.sparfuchsapp.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sparfuchsapp.ui.screens.MainScreen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(  //Creates list of all the tabs in the bottom bar
        BottomNavItem.Home,
        BottomNavItem.ProductSearch,
        BottomNavItem.Shopping,
        BottomNavItem.Scanner,
        BottomNavItem.Settings
    )

    NavigationBar{
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = {
                    Text(text = item.label, textAlign = TextAlign.Center) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

//Sets the items, routes and labels for the bottom bar
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("main", Icons.Default.Home, "Home")
    object ProductSearch : BottomNavItem("search", Icons.Default.Search, "Search")
    object Shopping : BottomNavItem("shopping", Icons.Default.ShoppingCart, "Grocery\nShopping")
    object Scanner : BottomNavItem("scanner", Icons.Default.Build, "Scanner")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}

@Preview(showBackground = true, name = "BottomNavBarPreview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = rememberNavController())
}