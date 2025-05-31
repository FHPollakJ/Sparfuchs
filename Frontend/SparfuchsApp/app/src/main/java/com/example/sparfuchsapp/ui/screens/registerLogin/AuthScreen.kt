package com.example.sparfuchsapp.ui.screens.registerLogin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparfuchsapp.utils.Routes
import kotlinx.coroutines.launch
import com.example.sparfuchsapp.R

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    padding: PaddingValues,
    navController: NavHostController
) {
    val user by viewModel.user.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loginLoading.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()



    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Routes.HOME) {
                popUpTo("auth") { inclusive = true } // remove Auth from backstack
                launchSingleTop = true // avoid multiple copies of same screen
            }
        }
    }

    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.padding(padding)
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sparfuchslogo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(
                    "Welcome to Sparfuchs!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Login")
                        Switch(
                            checked = isRegistering,
                            onCheckedChange = { isRegistering = it },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text("Register")
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                    )

                    if (isRegistering) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            singleLine = true,
                        )
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text("Password") },
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (loading) {
                        CircularProgressIndicator()
                    } else {
                        Row(
                            Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val isFormValid = email.isNotBlank() && password.isNotBlank()
                            Button(
                                onClick = {
                                    if (isRegistering) {
                                        viewModel.register(email, username, password)
                                    } else {
                                        viewModel.login(email, password)
                                    }
                                },
                                enabled = isFormValid
                            ) {
                                Text(if (isRegistering) "Register" else "Login")
                            }
                        }
                    }
                }
            }

            user?.let {
                Log.d("AuthScreen", "Logged in as: ${it.username}")
            }

            error?.let {
                Log.e("AuthScreen", "Error: $it")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(
        viewModel = viewModel(),padding = PaddingValues(0.dp), navController = NavHostController(LocalContext.current))
}