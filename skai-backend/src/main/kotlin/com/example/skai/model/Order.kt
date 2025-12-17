package com.example.skai.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @Column(length = 36)
    var id: String? = null,

    @Column(name = "user_id", nullable = false, length = 36)
    var userId: String,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference // Evita la serializaciï¿½n recursiva
    var items: MutableList<OrderItem> = mutableListOf(),

    @Column(name = "total_amount", nullable = false)
    var totalAmount: Double,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OrderStatus,

    @Column(name = "created_at")
    var createdAt: Long? = null,

    @Lob
    @Column(name = "shipping_address")
    var shippingAddress: String? = null,

    @Lob
    @Column
    var notes: String = ""
)
