package com.example.skai.data.repository

import com.example.skai.data.database.dao.CartDao
import com.example.skai.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getCartItemsByUserId(userId: String): Flow<List<CartItem>> {
        return cartDao.getCartItemsByUserId(userId)
    }

    suspend fun addToCart(cartItem: CartItem) {
        val existingItem = cartDao.getCartItem(cartItem.userId, cartItem.productId, cartItem.selectedSize)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
            cartDao.updateCartItem(updatedItem)
        } else {
            cartDao.insertCartItem(cartItem)
        }
    }

    suspend fun updateCartItemQuantity(userId: String, productId: String, size: String, quantity: Int) {
        val cartItem = cartDao.getCartItem(userId, productId, size)
        cartItem?.let {
            if (quantity <= 0) {
                cartDao.deleteCartItem(it)
            } else {
                cartDao.updateCartItem(it.copy(quantity = quantity))
            }
        }
    }

    suspend fun removeFromCart(userId: String, productId: String, size: String) {
        cartDao.removeCartItem(userId, productId, size)
    }

    suspend fun clearCart(userId: String) {
        cartDao.clearCart(userId)
    }

    suspend fun getCartItem(userId: String, productId: String, size: String): CartItem? {
        return cartDao.getCartItem(userId, productId, size)
    }
}
