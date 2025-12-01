package com.example.skai.service

import com.example.skai.model.CartItem
import com.example.skai.repository.CartItemRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartService(
    private val cartItemRepository: CartItemRepository
) {
    
    fun getCartItemsByUserId(userId: String): List<CartItem> {
        return cartItemRepository.findByUserId(userId)
    }
    
    fun addToCart(cartItem: CartItem): CartItem {
        val existingItem = cartItemRepository.findByUserIdAndProductIdAndSelectedSize(
            cartItem.userId,
            cartItem.productId,
            cartItem.selectedSize
        )
        
        return if (existingItem.isPresent) {
            val item = existingItem.get()
            val updatedItem = CartItem(
                id = item.id,
                userId = item.userId,
                productId = item.productId,
                productName = item.productName,
                productPrice = item.productPrice,
                productImage = item.productImage,
                selectedSize = item.selectedSize,
                quantity = item.quantity + cartItem.quantity
            )
            cartItemRepository.save(updatedItem)
        } else {
            val newItem = if (cartItem.id.isNullOrEmpty()) {
                CartItem(
                    id = UUID.randomUUID().toString(),
                    userId = cartItem.userId,
                    productId = cartItem.productId,
                    productName = cartItem.productName,
                    productPrice = cartItem.productPrice,
                    productImage = cartItem.productImage,
                    selectedSize = cartItem.selectedSize,
                    quantity = cartItem.quantity
                )
            } else {
                cartItem
            }
            cartItemRepository.save(newItem)
        }
    }
    
    fun updateCartItemQuantity(userId: String, productId: String, size: String, quantity: Int): CartItem {
        val cartItem = cartItemRepository.findByUserIdAndProductIdAndSelectedSize(userId, productId, size)
            .orElseThrow { RuntimeException("Cart item not found") }
        val updatedItem = CartItem(
            id = cartItem.id,
            userId = cartItem.userId,
            productId = cartItem.productId,
            productName = cartItem.productName,
            productPrice = cartItem.productPrice,
            productImage = cartItem.productImage,
            selectedSize = cartItem.selectedSize,
            quantity = quantity
        )
        return cartItemRepository.save(updatedItem)
    }
    
    fun removeFromCart(userId: String, productId: String, size: String) {
        cartItemRepository.deleteByUserIdAndProductIdAndSelectedSize(userId, productId, size)
    }
    
    fun clearCart(userId: String) {
        cartItemRepository.deleteByUserId(userId)
    }
    
    fun getCartItem(userId: String, productId: String, size: String): CartItem {
        return cartItemRepository.findByUserIdAndProductIdAndSelectedSize(userId, productId, size)
            .orElseThrow { RuntimeException("Cart item not found") }
    }
}

