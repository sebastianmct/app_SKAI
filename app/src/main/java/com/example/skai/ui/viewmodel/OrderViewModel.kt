package com.example.skai.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.DataManager
import com.example.skai.data.model.Order
import com.example.skai.data.model.OrderStatus
import com.example.skai.utils.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated.asStateFlow()

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _orders.value = DataManager.getOrders(userId)
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createOrder(
        userId: String,
        cartItems: List<com.example.skai.data.model.CartItem>,
        shippingAddress: String,
        notes: String = "",
        context: Context? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderItems = cartItems.map { cartItem ->
                    com.example.skai.data.model.OrderItem(
                        productId = cartItem.productId,
                        productName = cartItem.productName,
                        productPrice = cartItem.productPrice,
                        productImage = cartItem.productImage,
                        selectedSize = cartItem.selectedSize,
                        quantity = cartItem.quantity
                    )
                }

                val totalAmount = cartItems.sumOf { it.productPrice * it.quantity }

                val order = Order(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    items = orderItems,
                    totalAmount = totalAmount,
                    status = OrderStatus.PENDING,
                    shippingAddress = shippingAddress,
                    notes = notes
                )

                DataManager.createOrder(order)
                _orderCreated.value = true
                
                // Notificar confirmación de pedido
                context?.let {
                    NotificationService.notifyOrderConfirmed(it, order.id)
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetOrderCreated() {
        _orderCreated.value = false
    }

    suspend fun getOrderById(orderId: String): Order? {
        return DataManager.getOrderById(orderId)
    }
}
