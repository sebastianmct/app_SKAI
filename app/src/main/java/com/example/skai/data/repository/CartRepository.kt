package com.example.skai.data.repository

import com.example.skai.data.api.CartApiService
import com.example.skai.data.api.QuantityRequest
import com.example.skai.data.database.dao.CartDao
import com.example.skai.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao,
    private val cartApiService: CartApiService
) {
    fun getCartItemsByUserId(userId: String): Flow<List<CartItem>> {
        return flow {

            try {
                val response = cartApiService.getCartItemsByUserId(userId)
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    items.forEach { item ->
                        cartDao.insertCartItem(item)
                    }
                }
            } catch (e: Exception) {

            }
            emitAll(cartDao.getCartItemsByUserId(userId))
        }
    }

    suspend fun addToCart(cartItem: CartItem) {
        try {

            val response = cartApiService.addToCart(cartItem)
            if (response.isSuccessful) {
                val addedItem = response.body()
                addedItem?.let { cartDao.insertCartItem(it) }
            } else {

                val existingItem = cartDao.getCartItem(cartItem.userId, cartItem.productId, cartItem.selectedSize)
                if (existingItem != null) {
                    val updatedItem = existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
                    cartDao.updateCartItem(updatedItem)
                } else {
                    cartDao.insertCartItem(cartItem)
                }
            }
        } catch (e: Exception) {

            val existingItem = cartDao.getCartItem(cartItem.userId, cartItem.productId, cartItem.selectedSize)
            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
                cartDao.updateCartItem(updatedItem)
            } else {
                cartDao.insertCartItem(cartItem)
            }
        }
    }

    suspend fun updateCartItemQuantity(userId: String, productId: String, size: String, quantity: Int) {
        try {

            val response = cartApiService.updateCartItemQuantity(userId, productId, size, QuantityRequest(quantity))
            if (response.isSuccessful) {
                val updatedItem = response.body()
                if (quantity <= 0) {
                    updatedItem?.let { cartDao.deleteCartItem(it) }
                } else {
                    updatedItem?.let { cartDao.updateCartItem(it) }
                }
            } else {

                val cartItem = cartDao.getCartItem(userId, productId, size)
                cartItem?.let {
                    if (quantity <= 0) {
                        cartDao.deleteCartItem(it)
                    } else {
                        cartDao.updateCartItem(it.copy(quantity = quantity))
                    }
                }
            }
        } catch (e: Exception) {

            val cartItem = cartDao.getCartItem(userId, productId, size)
            cartItem?.let {
                if (quantity <= 0) {
                    cartDao.deleteCartItem(it)
                } else {
                    cartDao.updateCartItem(it.copy(quantity = quantity))
                }
            }
        }
    }

    suspend fun removeFromCart(userId: String, productId: String, size: String) {
        try {

            val response = cartApiService.removeFromCart(userId, productId, size)
            if (response.isSuccessful) {
                cartDao.removeCartItem(userId, productId, size)
            } else {

                cartDao.removeCartItem(userId, productId, size)
            }
        } catch (e: Exception) {

            cartDao.removeCartItem(userId, productId, size)
        }
    }

    suspend fun clearCart(userId: String) {
        try {

            val response = cartApiService.clearCart(userId)
            if (response.isSuccessful) {
                cartDao.clearCart(userId)
            } else {

                cartDao.clearCart(userId)
            }
        } catch (e: Exception) {

            cartDao.clearCart(userId)
        }
    }

    suspend fun getCartItem(userId: String, productId: String, size: String): CartItem? {
        return try {

            val response = cartApiService.getCartItem(userId, productId, size)
            if (response.isSuccessful) {
                val item = response.body()
                item?.let { cartDao.insertCartItem(it) }
                item
            } else {
                cartDao.getCartItem(userId, productId, size)
            }
        } catch (e: Exception) {
            cartDao.getCartItem(userId, productId, size)
        }
    }
}
