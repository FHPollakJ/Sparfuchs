package com.example.sparfuchsapp.ui.screens.registerLogin

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparfuchsapp.data.remote.RetrofitClient
import com.example.sparfuchsapp.data.remote.dto.AuthRequestDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.data.remote.dto.UserResponseDTO
import com.example.sparfuchsapp.data.remote.dto.UserStatsDTO
import com.example.sparfuchsapp.utils.translateErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserResponseDTO?>(null)
    val user: StateFlow<UserResponseDTO?> = _user
    private val _purchases = MutableStateFlow<List<PurchaseDTO>>(emptyList())
    val purchases:  StateFlow<List<PurchaseDTO>> = _purchases
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _userStats = MutableStateFlow<UserStatsDTO?>(null)
    val userStats: StateFlow<UserStatsDTO?> = _userStats
    private val _loginLoading = MutableStateFlow(false)
    val loginLoading: StateFlow<Boolean> = _loginLoading
    private val _purchaseLoading = MutableStateFlow(false)
    val purchaseLoading: StateFlow<Boolean> = _purchaseLoading

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.register(
                    AuthRequestDTO(email = email, password = password, username = username)
                )
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _error.value = null

                    login(email, password, manageLoading = false) // Login after registration, passing flag to avoid loading in register
                } else {
                    _error.value = "Registration failed: ${translateErrorMessage(response.errorBody())}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun login(email: String, password: String, manageLoading: Boolean = true) {
        viewModelScope.launch {
            if (manageLoading) _loginLoading.value = true
            try {
                val response = RetrofitClient.userApi.login(
                    AuthRequestDTO(email = email, password = password)
                )
                val headers = response.headers().toMultimap()
                Log.d("LoginHeaders", headers.toString())
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _error.value = null
                    //loadPurchases()
                    //getUserStats()
                    Log.d("LoginCookies", RetrofitClient.CookieManager.cookieStore.toString())
                } else {
                    _error.value = "Login failed: ${translateErrorMessage(response.errorBody())}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                if (manageLoading) _loginLoading.value = false
            }
        }
    }

    fun loadPurchases() {
        viewModelScope.launch {
            _purchaseLoading.value = true
            try {
                val response = RetrofitClient.userApi.getPurchases()
                if (response.isSuccessful) {
                    Log.d("PurchaseResponse", "Fetching purchases successful")
                    _purchases.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Failed to load purchases: ${translateErrorMessage(response.errorBody())}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _purchaseLoading.value = false
            }
        }
    }

    fun getUserStats() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.getStats()
                if (response.isSuccessful) {
                    val stats = response.body()
                    if (stats != null) {
                        _userStats.value = stats
                        _error.value = null
                    } else {
                        _error.value = "Failed to load stats: empty response"
                    }
                } else {
                    _error.value = "Failed to load stats: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
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