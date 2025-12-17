package com.example.skai.data.repository

import com.example.skai.data.api.ProductApiService
import com.example.skai.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService
) {

    suspend fun getAllActiveProducts(): List<Product> {
        val response = productApiService.getAllProducts()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getProductById(productId: String): Product? {
        val response = productApiService.getProductById(productId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        val response = productApiService.getProductsByCategory(category)
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun searchProducts(query: String): List<Product> {
        // El backend no tiene endpoint de búsqueda, así que filtramos localmente
        val allProducts = getAllActiveProducts()
        return allProducts.filter { product ->
            product.name.contains(query, ignoreCase = true) ||
            product.description?.contains(query, ignoreCase = true) == true
        }
    }

    suspend fun insertProduct(product: Product): Product? {
        val response = productApiService.createProduct(product)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun updateProduct(product: Product): Product? {
        val id = product.id ?: return null
        val response = productApiService.updateProduct(id, product)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun deleteProduct(product: Product): Boolean {
        val id = product.id ?: return false
        val response = productApiService.deleteProduct(id)
        return response.isSuccessful
    }

    suspend fun updateProductStock(productId: String, newStock: Int): Product? {
        val product = getProductById(productId) ?: return null
        val updatedProduct = product.copy(stock = newStock)
        return updateProduct(updatedProduct)
    }
}

