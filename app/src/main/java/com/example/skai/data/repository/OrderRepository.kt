package com.example.skai.data.repository

import com.example.skai.data.database.dao.OrderDao
import com.example.skai.data.model.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao
) {
    fun getOrdersByUserId(userId: String): Flow<List<Order>> {
        return orderDao.getOrdersByUserId(userId)
    }

    suspend fun getOrderById(orderId: String): Order? {
        return orderDao.getOrderById(orderId)
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders()
    }

    suspend fun createOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }
}
