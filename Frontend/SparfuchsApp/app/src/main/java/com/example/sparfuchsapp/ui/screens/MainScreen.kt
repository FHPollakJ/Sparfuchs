package com.example.sparfuchsapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.ui.theme.moneySavedLight
import com.example.sparfuchsapp.ui.components.PurchaseHistory
import com.example.sparfuchsapp.ui.screens.registerLogin.AuthViewModel
import com.example.sparfuchsapp.ui.theme.SparfuchsAppTheme
import java.time.LocalTime

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    padding: PaddingValues
) {
    val user by viewModel.user.collectAsState()
    val purchases by viewModel.purchases.collectAsState()
    val stats by viewModel.userStats.collectAsState()
    val loading by viewModel.purchaseLoading.collectAsState()
    val greeting = getGreeting()

    LaunchedEffect(user) {
        if (user != null) {
            viewModel.loadPurchases()
            viewModel.getUserStats()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "$greeting, ${user?.username}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ){
                    Text(
                        text = "Overall Spending",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "€ %.2f".format(stats?.totalAmountSpent ?: 0.0).replace('.',','),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = "Your Savings",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =  "€ %.2f".format(stats?.totalAmountSaved ?: 0.0).replace('.',','),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = moneySavedLight
                    )
                }
            }
            Text(
                text = "Shopping History",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (loading) {
                CircularProgressIndicator()
            } else {
                val completedPurchases = purchases.filter { it.isCompleted }
                if (completedPurchases.isEmpty()) {
                    Text("No completed purchases yet")
                } else {
                    PurchaseHistory(completedPurchases)
                }
            }
        }
    }
}

fun getGreeting(): String {
    val currentHour = LocalTime.now().hour
    return when (currentHour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Good night"
    }
}

@Preview(showBackground = true, name = "MainMenuPreview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainMenuScreenPreview() {
    SparfuchsAppTheme {
        MainScreen(viewModel = AuthViewModel(), padding = PaddingValues(0.dp))
    }
}
