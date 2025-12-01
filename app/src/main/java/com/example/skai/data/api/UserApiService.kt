package com.example.skai.data.api

import com.example.skai.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<User>
    
    @POST("users/register")
    suspend fun register(@Body user: User): Response<User>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>
    
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>
    
    @GET("users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<User>
}

data class LoginRequest(
    val email: String,
    val password: String
)

