package com.example.skai.data.database.dao

import androidx.room.*
import com.example.skai.data.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItemsByUserId(userId: String): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId AND selectedSize = :size")
    suspend fun getCartItem(userId: String, productId: String, size: String): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId AND selectedSize = :size")
    suspend fun removeCartItem(userId: String, productId: String, size: String)
}
