package com.example.skai.data.repository

import com.example.skai.data.database.dao.UserDao
import com.example.skai.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): User? {
        return userDao.getUserByCredentials(email, password)
    }

    suspend fun register(user: User): Boolean {
        return try {
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser == null) {
                userDao.insertUser(user)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
