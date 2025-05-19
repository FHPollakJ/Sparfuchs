package com.example.sparfuchsapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.ui.screens.viewModels.AuthViewModel
import com.example.sparfuchsapp.ui.theme.SavingsGreen
import java.time.LocalTime

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    padding: PaddingValues
) {
    val user by viewModel.user.collectAsState()
    val purchases by viewModel.purchases.collectAsState()
    val greeting = getGreeting()

    val savingsText = if(purchases.sumOf { it.totalSaved } != 0.0) {
        "€ %.2f".format(purchases.sumOf { it.totalSaved }).replace('.',',')
    } else {
        "Start saving :D"
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
                    Text(text = "Overall Spending")
                    Text(
                        text = "€ %.2f".format(purchases.sumOf { it.totalSpent }).replace('.',','),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Your Savings"
                    )

                    Text(
                        text = savingsText,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = SavingsGreen
                    )
                }
            }
            Text("History")

        }
    }
}

//If we need that function more often we should move it to a "TimeFunctions.kt" file in the Utils folder
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
    MainScreen(viewModel = AuthViewModel(), padding = PaddingValues(0.dp))
}
