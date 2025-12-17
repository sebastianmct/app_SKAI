package com.example.skai.model

import jakarta.persistence.*

@Entity
@Table(name = "cart_items")
data class CartItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false, length = 36)
    var userId: String,

    @Column(name = "product_id", nullable = false, length = 36)
    var productId: String,

    @Column(name = "product_name", nullable = false)
    var productName: String,

    @Column(name = "product_price", nullable = false)
    var productPrice: Double,

    @Column(name = "product_image", length = 1000)
    var productImage: String? = null,

    @Column(name = "selected_size", nullable = false, length = 20)
    var selectedSize: String,

    @Column(nullable = false)
    var quantity: Int
)
