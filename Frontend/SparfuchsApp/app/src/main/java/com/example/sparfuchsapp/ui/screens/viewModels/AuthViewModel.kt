package com.example.sparfuchsapp.ui.screens.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparfuchsapp.data.remote.RetrofitClient
import com.example.sparfuchsapp.data.remote.dto.AuthRequestDTO
import com.example.sparfuchsapp.data.remote.dto.UserResponseDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserResponseDTO?>(null)
    val user: StateFlow<UserResponseDTO?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.register(
                    AuthRequestDTO(email = email, password = password, username = username)
                )
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Registration failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.login(
                    AuthRequestDTO(email = email, password = password)
                )
                val headers = response.headers().toMultimap()
                Log.d("LoginHeaders", headers.toString())
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _error.value = null
                    Log.d("LoginCookies", RetrofitClient.CookieManager.cookieStore.toString())
                } else {
                    _error.value = "Login failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("logged_in", isLoggedIn).apply()
    }
}