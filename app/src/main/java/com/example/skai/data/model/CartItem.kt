package com.example.skai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cart_items")
@Parcelize
data class CartItem(
    @PrimaryKey
    val id: String,
    val userId: String,
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val selectedSize: String,
    val quantity: Int
) : Parcelable
