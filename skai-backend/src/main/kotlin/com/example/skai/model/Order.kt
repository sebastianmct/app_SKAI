package com.example.skai.model

import jakarta.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    var id: String? = null,
    
    @Column(name = "user_id", nullable = false)
    var userId: String,
    
    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = [JoinColumn(name = "order_id")])
    var items: List<OrderItem> = emptyList(),
    
    @Column(name = "total_amount", nullable = false)
    var totalAmount: Double,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,
    
    @Column(name = "created_at")
    var createdAt: Long? = null,
    
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    var shippingAddress: String? = null,
    
    @Column(columnDefinition = "TEXT")
    var notes: String = ""
)

@Embeddable
data class OrderItem(
    @Column(name = "product_id")
    var productId: String,
    
    @Column(name = "product_name")
    var productName: String,
    
    @Column(name = "product_price")
    var productPrice: Double,
    
    @Column(name = "product_image")
    var productImage: String? = null,
    
    @Column(name = "selected_size")
    var selectedSize: String,
    
    var quantity: Int
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

