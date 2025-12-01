package com.example.skai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String,
    val isAdmin: Boolean = false,
    val profileImageUri: String? = null
) : Parcelable
