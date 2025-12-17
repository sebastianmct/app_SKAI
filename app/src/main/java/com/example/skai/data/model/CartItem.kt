package com.example.skai.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartItem(
    val id: Long? = null,
    val userId: String,
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImage: String? = null,
    val selectedSize: String,
    val quantity: Int
) : Parcelable