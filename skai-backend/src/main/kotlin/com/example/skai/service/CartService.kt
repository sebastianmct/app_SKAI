package com.example.skai.service

import com.example.skai.model.CartItem
import com.example.skai.repository.CartItemRepository
import com.example.skai.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository // Para verificar detalles del producto
) {

    fun getCartItems(userId: String): List<CartItem> {
        return cartItemRepository.findByUserId(userId)
    }

    @Transactional
    fun addToCart(
        userId: String, 
        productId: String, 
        size: String, 
        quantity: Int,
        productName: String? = null,
        productPrice: Double? = null,
        productImage: String? = null
    ): CartItem {
        val existingItem = cartItemRepository.findByUserIdAndProductIdAndSelectedSize(userId, productId, size)

        return if (existingItem.isPresent) {
            // Si el item ya existe, actualiza la cantidad
            val item = existingItem.get()
            item.quantity += quantity
            cartItemRepository.save(item)
        } else {
            // Si es un item nuevo, usamos los datos proporcionados o buscamos el producto
            val name: String
            val price: Double
            val image: String?
            
            if (productName != null && productPrice != null) {
                name = productName
                price = productPrice
                image = productImage
            } else {
                val product = productRepository.findById(productId).orElseThrow { 
                    Exception("Producto no encontrado con id: $productId") 
                }
                name = product.name
                price = product.price
                image = product.images?.firstOrNull()
            }
            
            val newItem = CartItem(
                userId = userId,
                productId = productId,
                productName = name,
                productPrice = price,
                productImage = image,
                selectedSize = size,
                quantity = quantity
            )
            cartItemRepository.save(newItem)
        }
    }

    @Transactional
    fun updateQuantity(userId: String, productId: String, size: String, quantity: Int): CartItem? {
        val existingItem = cartItemRepository.findByUserIdAndProductIdAndSelectedSize(userId, productId, size)
            .orElseThrow { Exception("Item del carrito no encontrado") }

        if (quantity <= 0) {
            // Si la cantidad es 0 o menos, eliminamos el item
            cartItemRepository.delete(existingItem)
            return null
        } else {
            existingItem.quantity = quantity
            return cartItemRepository.save(existingItem)
        }
    }

    @Transactional
    fun removeFromCart(userId: String, productId: String, size: String) {
        cartItemRepository.deleteByUserIdAndProductIdAndSelectedSize(userId, productId, size)
    }

    @Transactional
    fun clearCart(userId: String) {
        cartItemRepository.deleteByUserId(userId)
    }
}
