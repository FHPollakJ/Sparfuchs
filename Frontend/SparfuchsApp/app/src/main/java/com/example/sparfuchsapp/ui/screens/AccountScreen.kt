package com.example.sparfuchsapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.ui.screens.registerLogin.AuthViewModel
import com.example.sparfuchsapp.ui.theme.SparfuchsAppTheme

@Composable
fun AccountScreen(
    padding: PaddingValues,
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Username: ${viewModel.user.value?.username ?: "-"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Email: ${viewModel.user.value?.email ?: "-"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                }
            ) {Text("Logout")}
        }
    }
}

@Preview(showBackground = true, name = "MainMenuPreview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AccountScreenScreenPreview(){
    SparfuchsAppTheme {
        AccountScreen(
            padding = PaddingValues(0.dp),
            viewModel = AuthViewModel(),
            onLogout = {}
        )
    }
}