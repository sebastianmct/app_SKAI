package com.example.skai.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(

    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String,
    val isAdmin: Boolean = false,
    val profileImageUri: String? = null
) : Parcelable
