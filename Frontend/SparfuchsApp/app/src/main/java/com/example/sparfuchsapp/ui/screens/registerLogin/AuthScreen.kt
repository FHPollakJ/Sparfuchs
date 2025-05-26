package com.example.sparfuchsapp.ui.screens.registerLogin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparfuchsapp.utils.Routes

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    padding: PaddingValues,
    navController: NavHostController
) {
    val user by viewModel.user.collectAsState()
    val error by viewModel.error.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Routes.HOME) {
                popUpTo("auth") { inclusive = true } // remove Auth from backstack
                launchSingleTop = true // avoid multiple copies of same screen
            }
        }
    }

    Column(
        Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })

        OutlinedTextField(value = password, onValueChange = { password = it }, visualTransformation = PasswordVisualTransformation(), label = { Text("Password") })

        Spacer(modifier = Modifier.height(8.dp))
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Button(onClick = { viewModel.login(email, password) }) {
                Text("Login")
            }

            Button(onClick = { viewModel.register(email, username, password) }) {
                Text("Register")
            }
        }

        user?.let {
            Text("Logged in as: ${it.username}")
            Log.d("AuthScreen", "Logged in as: ${it.username}")
        }

        error?.let {
            Text("Error: $it", color = Color.Red)
            Log.e("AuthScreen", "Error: $it")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(
        viewModel = viewModel(),padding = PaddingValues(0.dp), navController = NavHostController(LocalContext.current))
}