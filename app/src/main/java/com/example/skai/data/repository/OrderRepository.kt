package com.example.skai.data.repository

import com.example.skai.data.api.OrderApiService
import com.example.skai.data.database.dao.OrderDao
import com.example.skai.data.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderApiService: OrderApiService
) {
    fun getOrdersByUserId(userId: String): Flow<List<Order>> {
        return flow {

            try {
                val response = orderApiService.getOrdersByUserId(userId)
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    orders.forEach { order ->
                        orderDao.insertOrder(order)
                    }
                }
            } catch (e: Exception) {

            }
            emitAll(orderDao.getOrdersByUserId(userId))
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return try {

            val response = orderApiService.getOrderById(orderId)
            if (response.isSuccessful) {
                val order = response.body()
                order?.let { orderDao.insertOrder(it) }
                order
            } else {
                orderDao.getOrderById(orderId)
            }
        } catch (e: Exception) {
            orderDao.getOrderById(orderId)
        }
    }

    fun getAllOrders(): Flow<List<Order>> {
        return flow {

            try {
                val response = orderApiService.getAllOrders()
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    orders.forEach { order ->
                        orderDao.insertOrder(order)
                    }
                }
            } catch (e: Exception) {

            }
            emitAll(orderDao.getAllOrders())
        }
    }

    suspend fun createOrder(order: Order) {
        try {

            val response = orderApiService.createOrder(order)
            if (response.isSuccessful) {
                val createdOrder = response.body()
                createdOrder?.let { orderDao.insertOrder(it) }
            } else {

                orderDao.insertOrder(order)
            }
        } catch (e: Exception) {

            orderDao.insertOrder(order)
        }
    }

    suspend fun updateOrder(order: Order) {
        try {

            val response = orderApiService.updateOrder(order.id, order)
            if (response.isSuccessful) {
                val updatedOrder = response.body()
                updatedOrder?.let { orderDao.updateOrder(it) }
            } else {

                orderDao.updateOrder(order)
            }
        } catch (e: Exception) {

            orderDao.updateOrder(order)
        }
    }

    suspend fun deleteOrder(order: Order) {
        try {

            val response = orderApiService.deleteOrder(order.id)
            if (response.isSuccessful) {
                orderDao.deleteOrder(order)
            } else {

                orderDao.deleteOrder(order)
            }
        } catch (e: Exception) {

            orderDao.deleteOrder(order)
        }
    }
}
