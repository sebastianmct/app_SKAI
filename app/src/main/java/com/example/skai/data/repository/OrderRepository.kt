package com.example.skai.data.repository

import com.example.skai.data.api.OrderApiService
import com.example.skai.data.model.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderApiService: OrderApiService
) {
    
    suspend fun getOrdersByUserId(userId: String): List<Order> {
        val response = orderApiService.getOrdersByUserId(userId)
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        val response = orderApiService.getOrderById(orderId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getAllOrders(): List<Order> {
        val response = orderApiService.getAllOrders()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun createOrder(order: Order): Order? {
        val response = orderApiService.createOrder(order)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun updateOrder(order: Order): Order? {
        val id = order.id ?: return null
        val response = orderApiService.updateOrder(id, order)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun deleteOrder(order: Order): Boolean {
        val id = order.id ?: return false
        val response = orderApiService.deleteOrder(id)
        return response.isSuccessful
    }
}
