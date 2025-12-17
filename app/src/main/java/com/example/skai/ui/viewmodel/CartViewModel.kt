package com.example.skai.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.data.model.CartItem
import com.example.skai.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CartViewModel"
    }

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
                Log.d(TAG, "Loading cart items for user: $userId")
                val items = cartRepository.getCartItemsByUserId(userId)
                Log.d(TAG, "Loaded ${items.size} cart items")
                _cartItems.value = items
                calculateTotalAmount(items)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading cart items: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(TAG, "Adding to cart: userId=${cartItem.userId}, productId=${cartItem.productId}, size=${cartItem.selectedSize}")
                val result = cartRepository.addToCart(cartItem)
                if (result != null) {
                    Log.d(TAG, "Successfully added to cart, reloading items")
                    loadCartItems(cartItem.userId)
                } else {
                    Log.e(TAG, "Failed to add to cart - result was null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding to cart: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateQuantity(userId: String, productId: String, size: String, quantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = cartRepository.updateCartItemQuantity(userId, productId, size, quantity)
                if (result != null) {
                    loadCartItems(userId)
                }
            } catch (e: Exception) {
                // Error handling
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFromCart(userId: String, productId: String, size: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = cartRepository.removeFromCart(userId, productId, size)
                if (success) {
                    loadCartItems(userId)
                }
            } catch (e: Exception) {
                // Error handling
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = cartRepository.clearCart(userId)
                if (success) {
                    loadCartItems(userId)
                }
            } catch (e: Exception) {
                // Error handling
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateTotalAmount(items: List<CartItem>) {
        val total = items.sumOf { it.productPrice * it.quantity }
        _totalAmount.value = total
    }

    fun getCartItemCount(userId: String): Int {
        return _cartItems.value.filter { it.userId == userId }.sumOf { it.quantity }
    }
}
