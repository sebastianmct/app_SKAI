package com.example.skai.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


data class ExternalProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)

interface ExternalProductApiService {
    
    @GET("products")
    suspend fun getAllProducts(): Response<List<ExternalProduct>>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ExternalProduct>
    
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<ExternalProduct>>
    
    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>
}

