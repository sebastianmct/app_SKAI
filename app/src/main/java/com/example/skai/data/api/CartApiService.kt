package com.example.skai.data.api

import com.example.skai.data.model.CartItem
import retrofit2.Response
import retrofit2.http.*

interface CartApiService {
    
    @GET("cart/user/{userId}")
    suspend fun getCartItemsByUserId(@Path("userId") userId: String): Response<List<CartItem>>
    
    @POST("cart")
    suspend fun addToCart(@Body cartItem: CartItem): Response<CartItem>
    
    @PUT("cart/{userId}/{productId}/{size}")
    suspend fun updateCartItemQuantity(
        @Path("userId") userId: String,
        @Path("productId") productId: String,
        @Path("size") size: String,
        @Body quantityRequest: QuantityRequest
    ): Response<CartItem>
    
    @DELETE("cart/{userId}/{productId}/{size}")
    suspend fun removeFromCart(
        @Path("userId") userId: String,
        @Path("productId") productId: String,
        @Path("size") size: String
    ): Response<Unit>
    
    @DELETE("cart/user/{userId}")
    suspend fun clearCart(@Path("userId") userId: String): Response<Unit>
    
    @GET("cart/{userId}/{productId}/{size}")
    suspend fun getCartItem(
        @Path("userId") userId: String,
        @Path("productId") productId: String,
        @Path("size") size: String
    ): Response<CartItem>
}

data class QuantityRequest(
    val quantity: Int
)

