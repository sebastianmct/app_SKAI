package com.example.skai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "orders")
@Parcelize
data class Order(
    @PrimaryKey
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val shippingAddress: String,
    val notes: String = ""
) : Parcelable

@Parcelize
data class OrderItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val selectedSize: String,
    val quantity: Int
) : Parcelable

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
