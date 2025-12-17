package com.example.skai.controller

import com.example.skai.model.CartItem
import com.example.skai.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {

    @GetMapping("/{userId}")
    fun getCartItems(@PathVariable userId: String): ResponseEntity<List<CartItem>> {
        val items = cartService.getCartItems(userId)
        return ResponseEntity.ok(items)
    }

    @PostMapping("/add")
    fun addToCart(@RequestBody request: AddToCartRequest): ResponseEntity<CartItem> {
        val newItem = cartService.addToCart(
            userId = request.userId, 
            productId = request.productId, 
            size = request.size, 
            quantity = request.quantity,
            productName = request.productName,
            productPrice = request.productPrice,
            productImage = request.productImage
        )
        return ResponseEntity.ok(newItem)
    }

    @PutMapping("/update")
    fun updateQuantity(@RequestBody request: UpdateQuantityRequest): ResponseEntity<CartItem?> {
        val updatedItem = cartService.updateQuantity(request.userId, request.productId, request.size, request.quantity)
        return ResponseEntity.ok(updatedItem)
    }

    @DeleteMapping("/remove")
    fun removeFromCart(@RequestBody request: RemoveFromCartRequest): ResponseEntity<Unit> {
        cartService.removeFromCart(request.userId, request.productId, request.size)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/clear/{userId}")
    fun clearCart(@PathVariable userId: String): ResponseEntity<Unit> {
        cartService.clearCart(userId)
        return ResponseEntity.ok().build()
    }
}

// Clases de solicitud para un mapeo de JSON más limpio
data class AddToCartRequest(
    val userId: String, 
    val productId: String, 
    val size: String, 
    val quantity: Int,
    val productName: String? = null,
    val productPrice: Double? = null,
    val productImage: String? = null
)
data class UpdateQuantityRequest(val userId: String, val productId: String, val size: String, val quantity: Int)
data class RemoveFromCartRequest(val userId: String, val productId: String, val size: String)
