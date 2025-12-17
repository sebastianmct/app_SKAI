package com.example.skai.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference // Evita la serializaciï¿½n recursiva
    var order: Order? = null,

    @Column(name = "product_id", length = 36, nullable = false)
    var productId: String,

    @Column(name = "product_name", length = 255, nullable = false)
    var productName: String,

    @Column(name = "product_price", nullable = false)
    var productPrice: Double,

    @Column(name = "product_image", length = 1000)
    var productImage: String? = null,

    @Column(name = "selected_size", length = 20, nullable = false)
    var selectedSize: String,

    @Column(nullable = false)
    var quantity: Int
)
