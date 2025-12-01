package com.example.skai.data.api

import com.example.skai.data.model.Order
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {
    
    @GET("orders/user/{userId}")
    suspend fun getOrdersByUserId(@Path("userId") userId: String): Response<List<Order>>
    
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<Order>
    
    @GET("orders")
    suspend fun getAllOrders(): Response<List<Order>>
    
    @POST("orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
    
    @PUT("orders/{id}")
    suspend fun updateOrder(@Path("id") id: String, @Body order: Order): Response<Order>
    
    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") id: String): Response<Unit>
}

