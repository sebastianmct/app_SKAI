package com.example.skai.data.repository

import com.example.skai.data.api.LoginRequest
import com.example.skai.data.api.UserApiService
import com.example.skai.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    
    suspend fun login(email: String, password: String): User? {
        val response = userApiService.login(LoginRequest(email, password))
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun register(user: User): User? {
        val response = userApiService.register(user)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getUserById(userId: String): User? {
        val response = userApiService.getUserById(userId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun updateUser(user: User): User? {
        val id = user.id ?: return null
        val response = userApiService.updateUser(id, user)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        val response = userApiService.getUserByEmail(email)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
