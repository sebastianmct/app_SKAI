package com.example.skai.model

import jakarta.persistence.*

@Entity
@Table(name = "cart_items")
data class CartItem(
    @Id
    var id: String? = null,
    
    @Column(name = "user_id", nullable = false)
    var userId: String,
    
    @Column(name = "product_id", nullable = false)
    var productId: String,
    
    @Column(name = "product_name", nullable = false)
    var productName: String,
    
    @Column(name = "product_price", nullable = false)
    var productPrice: Double,
    
    @Column(name = "product_image")
    var productImage: String? = null,
    
    @Column(name = "selected_size", nullable = false)
    var selectedSize: String,
    
    @Column(nullable = false)
    var quantity: Int
)

