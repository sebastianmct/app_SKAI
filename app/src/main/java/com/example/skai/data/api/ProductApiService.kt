package com.example.skai.data.api

import com.example.skai.data.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {
    
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<Product>
    
    @GET("products")
    suspend fun getProductsByCategory(@Query("category") category: String): Response<List<Product>>
    
    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>
    
    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: Product): Response<Product>
    
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>
}

