package com.example.skai.data.repository

import com.example.skai.data.api.LoginRequest
import com.example.skai.data.api.UserApiService
import com.example.skai.data.database.dao.UserDao
import com.example.skai.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService
) {
    suspend fun login(email: String, password: String): User? {
        return try {

            val response = userApiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val user = response.body()
                user?.let { userDao.insertUser(it) }
                user
            } else {

                userDao.getUserByCredentials(email, password)
            }
        } catch (e: Exception) {

            userDao.getUserByCredentials(email, password)
        }
    }

    suspend fun register(user: User): Boolean {
        return try {

            val response = userApiService.register(user)
            if (response.isSuccessful) {
                val registeredUser = response.body()
                registeredUser?.let { userDao.insertUser(it) }
                true
            } else {

                val existingUser = userDao.getUserByEmail(user.email)
                if (existingUser == null) {
                    userDao.insertUser(user)
                    true
                } else {
                    false
                }
            }
        } catch (e: Exception) {

            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser == null) {
                userDao.insertUser(user)
                true
            } else {
                false
            }
        }
    }

    suspend fun getUserById(userId: String): User? {
        return try {

            val response = userApiService.getUserById(userId)
            if (response.isSuccessful) {
                val user = response.body()
                user?.let { userDao.insertUser(it) }
                user
            } else {
                userDao.getUserById(userId)
            }
        } catch (e: Exception) {
            userDao.getUserById(userId)
        }
    }

    suspend fun updateUser(user: User) {
        try {

            val response = userApiService.updateUser(user.id, user)
            if (response.isSuccessful) {
                val updatedUser = response.body()
                updatedUser?.let { userDao.updateUser(it) }
            } else {

                userDao.updateUser(user)
            }
        } catch (e: Exception) {

            userDao.updateUser(user)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {

            val response = userApiService.getUserByEmail(email)
            if (response.isSuccessful) {
                val user = response.body()
                user?.let { userDao.insertUser(it) }
                user
            } else {
                userDao.getUserByEmail(email)
            }
        } catch (e: Exception) {
            userDao.getUserByEmail(email)
        }
    }
}
