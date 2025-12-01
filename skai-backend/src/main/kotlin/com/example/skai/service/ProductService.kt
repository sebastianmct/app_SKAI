package com.example.skai.service

import com.example.skai.model.Product
import com.example.skai.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }
    
    fun getProductsByCategory(category: String): List<Product> {
        return productRepository.findByCategory(category)
    }
    
    fun getProductById(id: String): Product {
        return productRepository.findById(id)
            .orElseThrow { RuntimeException("Product not found") }
    }
    
    fun createProduct(product: Product): Product {
        val newProduct = Product(
            id = if (product.id.isNullOrEmpty()) UUID.randomUUID().toString() else product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            category = product.category,
            sizes = product.sizes,
            images = product.images,
            stock = product.stock,
            isActive = product.isActive,
            createdAt = product.createdAt ?: System.currentTimeMillis()
        )
        return productRepository.save(newProduct)
    }
    
    fun updateProduct(id: String, product: Product): Product {
        val existingProduct = getProductById(id)
        val updatedProduct = Product(
            id = existingProduct.id,
            name = product.name,
            description = product.description,
            price = product.price,
            category = product.category,
            sizes = product.sizes,
            images = product.images,
            stock = product.stock,
            isActive = product.isActive,
            createdAt = existingProduct.createdAt
        )
        return productRepository.save(updatedProduct)
    }
    
    fun deleteProduct(id: String) {
        val product = getProductById(id)
        productRepository.delete(product)
    }
}

