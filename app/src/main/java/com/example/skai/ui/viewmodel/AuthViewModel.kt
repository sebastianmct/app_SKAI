package com.example.skai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.DataManager
import com.example.skai.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        _currentUser.value = DataManager.currentUser
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val user = DataManager.login(email, password)
                if (user != null) {
                    DataManager.setCurrentUser(user)
                    _currentUser.value = user
                } else {
                    _errorMessage.value = "Credenciales inválidas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val success = DataManager.register(user)
                if (success) {
                    DataManager.setCurrentUser(user)
                    _currentUser.value = user
                } else {
                    _errorMessage.value = "El email ya está registrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrarse: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        DataManager.setCurrentUser(null)
        _currentUser.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val success = DataManager.updateUser(user)
                if (success) {
                    if (user.id == _currentUser.value?.id) {
                        DataManager.setCurrentUser(user)
                        _currentUser.value = user
                    }
                } else {
                    _errorMessage.value = "Error al actualizar el usuario"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
