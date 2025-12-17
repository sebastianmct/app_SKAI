package com.example.skai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.data.model.User
import com.example.skai.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
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
                val registeredUser = userRepository.register(user)
                if (registeredUser != null) {
                    _currentUser.value = registeredUser
                } else {
                    _errorMessage.value = "El email ya está registrado o hubo un error"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrarse: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
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
                val updatedUser = userRepository.updateUser(user)
                if (updatedUser != null) {
                    if (user.id == _currentUser.value?.id) {
                        _currentUser.value = updatedUser
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

    // Permite establecer el usuario actual manualmente (por ejemplo al iniciar la app)
    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }
}
