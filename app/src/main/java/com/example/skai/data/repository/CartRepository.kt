package com.example.skai.data.repository

import android.util.Log
import com.example.skai.data.api.CartApiService
import com.example.skai.data.api.AddToCartRequest
import com.example.skai.data.api.UpdateQuantityRequest
import com.example.skai.data.api.RemoveFromCartRequest
import com.example.skai.data.model.CartItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartApiService: CartApiService
) {
    companion object {
        private const val TAG = "CartRepository"
    }

    suspend fun getCartItemsByUserId(userId: String): List<CartItem> {
        Log.d(TAG, "Getting cart items for userId: $userId")
        val response = cartApiService.getCartItemsByUserId(userId)
        return if (response.isSuccessful) {
            val items = response.body() ?: emptyList()
            Log.d(TAG, "Got ${items.size} items from API")
            items
        } else {
            Log.e(TAG, "Failed to get cart items: ${response.code()} - ${response.errorBody()?.string()}")
            emptyList()
        }
    }

    suspend fun addToCart(cartItem: CartItem): CartItem? {
        val request = AddToCartRequest(
            userId = cartItem.userId,
            productId = cartItem.productId,
            size = cartItem.selectedSize,
            quantity = cartItem.quantity,
            productName = cartItem.productName,
            productPrice = cartItem.productPrice,
            productImage = cartItem.productImage
        )
        Log.d(TAG, "Adding to cart: $request")
        val response = cartApiService.addToCart(request)
        return if (response.isSuccessful) {
            val result = response.body()
            Log.d(TAG, "Successfully added to cart: $result")
            result
        } else {
            Log.e(TAG, "Failed to add to cart: ${response.code()} - ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun updateCartItemQuantity(userId: String, productId: String, size: String, quantity: Int): CartItem? {
        val request = UpdateQuantityRequest(userId, productId, size, quantity)
        val response = cartApiService.updateCartItemQuantity(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun removeFromCart(userId: String, productId: String, size: String): Boolean {
        val request = RemoveFromCartRequest(userId, productId, size)
        val response = cartApiService.removeFromCart(request)
        return response.isSuccessful
    }

    suspend fun clearCart(userId: String): Boolean {
        val response = cartApiService.clearCart(userId)
        return response.isSuccessful
    }
}
