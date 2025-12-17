package com.example.skai.data.api

import com.example.skai.data.model.CartItem
import retrofit2.Response
import retrofit2.http.*

// --- Data classes para las peticiones (Request Bodies) ---
data class AddToCartRequest(
    val userId: String, 
    val productId: String, 
    val size: String, 
    val quantity: Int,
    val productName: String,
    val productPrice: Double,
    val productImage: String?
)
data class UpdateQuantityRequest(val userId: String, val productId: String, val size: String, val quantity: Int)
data class RemoveFromCartRequest(val userId: String, val productId: String, val size: String)

interface CartApiService {

    @GET("cart/{userId}")
    suspend fun getCartItemsByUserId(@Path("userId") userId: String): Response<List<CartItem>>

    @POST("cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<CartItem>

    @PUT("cart/update")
    suspend fun updateCartItemQuantity(@Body request: UpdateQuantityRequest): Response<CartItem>

    @HTTP(method = "DELETE", path = "cart/remove", hasBody = true)
    suspend fun removeFromCart(@Body request: RemoveFromCartRequest): Response<Unit>

    @DELETE("cart/clear/{userId}")
    suspend fun clearCart(@Path("userId") userId: String): Response<Unit>

    // Este endpoint ya no existe en el backend, la lï¿½gica se maneja localmente o con getCartItemsByUserId
    // suspend fun getCartItem(...)
}
