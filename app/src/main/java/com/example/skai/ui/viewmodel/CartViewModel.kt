package com.example.skai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.DataManager
import com.example.skai.data.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadCartItems(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _cartItems.value = DataManager.getCartItems(userId)
                calculateTotalAmount(_cartItems.value)
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            try {
                DataManager.addToCart(cartItem)
                loadCartItems(cartItem.userId)
            } catch (e: Exception) {

            }
        }
    }

    fun updateQuantity(userId: String, productId: String, size: String, quantity: Int) {
        viewModelScope.launch {
            try {
                DataManager.updateCartItemQuantity(userId, productId, size, quantity)
                loadCartItems(userId)
            } catch (e: Exception) {

            }
        }
    }

    fun removeFromCart(userId: String, productId: String, size: String) {
        viewModelScope.launch {
            try {
                DataManager.removeFromCart(userId, productId, size)
                loadCartItems(userId)
            } catch (e: Exception) {

            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            try {
                DataManager.clearCart(userId)
                loadCartItems(userId)
            } catch (e: Exception) {

            }
        }
    }

    private fun calculateTotalAmount(items: List<CartItem>) {
        val total = items.sumOf { it.productPrice * it.quantity }
        _totalAmount.value = total
    }

    fun getCartItemCount(userId: String): Int {
        return DataManager.getCartItemCount(userId)
    }
}
