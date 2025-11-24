package com.example.skai.data.repository

import com.example.skai.data.api.ExternalProduct
import com.example.skai.data.api.ExternalProductApiService
import com.example.skai.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para productos de API externa
 * Convierte productos externos al modelo interno de la app
 */
@Singleton
class ExternalProductRepository @Inject constructor(
    private val externalApiService: ExternalProductApiService
) {
    
    /**
     * Obtiene todos los productos de la API externa
     */
    suspend fun getAllExternalProducts(): List<Product> {
        return try {
            val response = externalApiService.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.map { it.toProduct() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Obtiene productos por categoría de la API externa
     */
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
    
    /**
     * Obtiene categorías disponibles de la API externa
     */
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
    
    /**
     * Convierte un producto externo al modelo interno
     */
    private fun ExternalProduct.toProduct(): Product {
        // Mapear categorías de la API externa a categorías de SKAI
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
            price = price * 1000, // Convertir de USD a CLP aproximado (1 USD = 1000 CLP)
            category = skaiCategory,
            sizes = listOf("S", "M", "L", "XL"), // Tamaños por defecto
            images = listOf(image),
            stock = rating.count, // Usar el count de rating como stock aproximado
            isActive = true
        )
    }
}

