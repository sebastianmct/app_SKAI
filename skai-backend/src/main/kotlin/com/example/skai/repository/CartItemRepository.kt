package com.example.skai.repository

import com.example.skai.model.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CartItemRepository : JpaRepository<CartItem, String> {
    fun findByUserId(userId: String): List<CartItem>
    fun findByUserIdAndProductIdAndSelectedSize(userId: String, productId: String, size: String): Optional<CartItem>
    fun deleteByUserId(userId: String)
    fun deleteByUserIdAndProductIdAndSelectedSize(userId: String, productId: String, size: String)
}

