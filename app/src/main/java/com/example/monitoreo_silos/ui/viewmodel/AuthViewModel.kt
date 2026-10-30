package com.example.monitoreo_silos.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoreo_silos.data.local.UserEntity
import com.example.monitoreo_silos.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    var currentUser by mutableStateOf<UserEntity?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repository.login(username, password)
            if (user != null) {
                currentUser = user
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = "Credenciales incorrectas"
            }
        }
    }

    fun register(user: UserEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = repository.register(user)
            if (success) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = "El nombre de usuario ya está registrado"
            }
        }
    }

    fun logout() {
        currentUser = null
    }
}