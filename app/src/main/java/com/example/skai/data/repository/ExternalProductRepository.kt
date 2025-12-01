package com.example.skai.data.repository

import com.example.skai.data.api.ExternalProduct
import com.example.skai.data.api.ExternalProductApiService
import com.example.skai.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExternalProductRepository @Inject constructor(
    private val externalApiService: ExternalProductApiService
) {
    
  
    suspend fun getAllExternalProducts(): List<Product> {
        return try {
            
            val mensClothingResponse = externalApiService.getProductsByCategory("men's clothing")
            val mensProducts = if (mensClothingResponse.isSuccessful) {
                mensClothingResponse.body()?.map { it.toProduct() } ?: emptyList()
            } else {
                emptyList()
            }
            

            val womensClothingResponse = externalApiService.getProductsByCategory("women's clothing")
            val womensProducts = if (womensClothingResponse.isSuccessful) {
                womensClothingResponse.body()?.map { it.toProduct() } ?: emptyList()
            } else {
                emptyList()
            }
            
            
            mensProducts + womensProducts
        } catch (e: Exception) {
            emptyList()
        }
    }
    
   
    suspend fun getExternalProductsByCategory(category: String): List<Product> {
        return try {
            val response = externalApiService.getProductsByCategory(category)
            if (response.isSuccessful) {
                response.body()?.map { it.toProduct() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
   
    suspend fun getExternalCategories(): List<String> {
        return try {
            val response = externalApiService.getCategories()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
   
    private fun ExternalProduct.toProduct(): Product {

        val skaiCategory = when (category.lowercase()) {
            "men's clothing" -> "Ropa"
            "women's clothing" -> "Ropa"
            "electronics" -> "Accesorios"
            "jewelery" -> "Accesorios"
            else -> "Ropa"
        }
        
        return Product(
            id = "external_${id}",
            name = title,
            description = description,
            price = price * 1000,
            category = skaiCategory,
            sizes = listOf("S", "M", "L", "XL"),
            images = listOf(image),
            stock = rating.count,
            isActive = true
        )
    }
}

