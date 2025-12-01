package com.example.skai.controller

import com.example.skai.model.CartItem
import com.example.skai.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {
    
    @GetMapping("/user/{userId}")
    fun getCartItemsByUserId(@PathVariable userId: String): ResponseEntity<List<CartItem>> {
        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId))
    }
    
    @PostMapping
    fun addToCart(@RequestBody cartItem: CartItem): ResponseEntity<CartItem> {
        val createdItem = cartService.addToCart(cartItem)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem)
    }
    
    @PutMapping("/{userId}/{productId}/{size}")
    fun updateCartItemQuantity(
        @PathVariable userId: String,
        @PathVariable productId: String,
        @PathVariable size: String,
        @RequestBody quantityRequest: Map<String, Int>
    ): ResponseEntity<Any> {
        return try {
            val quantity = quantityRequest["quantity"] ?: throw IllegalArgumentException("Quantity is required")
            val updatedItem = cartService.updateCartItemQuantity(userId, productId, size, quantity)
            ResponseEntity.ok(updatedItem)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Cart item not found")))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf<String, String>("error" to (e.message ?: "Invalid request")))
        }
    }
    
    @DeleteMapping("/{userId}/{productId}/{size}")
    fun removeFromCart(
        @PathVariable userId: String,
        @PathVariable productId: String,
        @PathVariable size: String
    ): ResponseEntity<Any> {
        return try {
            cartService.removeFromCart(userId, productId, size)
            ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Cart item not found")))
        }
    }
    
    @DeleteMapping("/user/{userId}")
    fun clearCart(@PathVariable userId: String): ResponseEntity<Any> {
        cartService.clearCart(userId)
        return ResponseEntity.ok().build()
    }
    
    @GetMapping("/{userId}/{productId}/{size}")
    fun getCartItem(
        @PathVariable userId: String,
        @PathVariable productId: String,
        @PathVariable size: String
    ): ResponseEntity<Any> {
        return try {
            val cartItem = cartService.getCartItem(userId, productId, size)
            ResponseEntity.ok(cartItem)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Cart item not found")))
        }
    }
}

