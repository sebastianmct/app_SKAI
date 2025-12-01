package com.example.skai.service

import com.example.skai.model.Order
import com.example.skai.model.OrderStatus
import com.example.skai.repository.OrderRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    
    fun getOrdersByUserId(userId: String): List<Order> {
        return orderRepository.findByUserId(userId)
    }
    
    fun getOrderById(id: String): Order {
        return orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found") }
    }
    
    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }
    
    fun createOrder(order: Order): Order {
        val newOrder = Order(
            id = if (order.id.isNullOrEmpty()) UUID.randomUUID().toString() else order.id,
            userId = order.userId,
            items = order.items,
            totalAmount = order.totalAmount,
            status = if (order.status != null) order.status else OrderStatus.PENDING,
            createdAt = order.createdAt ?: System.currentTimeMillis(),
            shippingAddress = order.shippingAddress,
            notes = order.notes
        )
        return orderRepository.save(newOrder)
    }
    
    fun updateOrder(id: String, order: Order): Order {
        val existingOrder = getOrderById(id)
        val updatedOrder = Order(
            id = existingOrder.id,
            userId = existingOrder.userId,
            items = order.items,
            totalAmount = order.totalAmount,
            status = order.status,
            createdAt = existingOrder.createdAt,
            shippingAddress = order.shippingAddress,
            notes = order.notes
        )
        return orderRepository.save(updatedOrder)
    }
    
    fun deleteOrder(id: String) {
        val order = getOrderById(id)
        orderRepository.delete(order)
    }
}

