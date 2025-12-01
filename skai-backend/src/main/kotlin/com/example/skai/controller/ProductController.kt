package com.example.skai.controller

import com.example.skai.model.Product
import com.example.skai.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    
    @GetMapping
    fun getAllProducts(@RequestParam(required = false) category: String?): ResponseEntity<List<Product>> {
        return if (category != null && category.isNotEmpty()) {
            ResponseEntity.ok(productService.getProductsByCategory(category))
        } else {
            ResponseEntity.ok(productService.getAllProducts())
        }
    }
    
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val product = productService.getProductById(id)
            ResponseEntity.ok(product)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Product not found")))
        }
    }
    
    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val createdProduct = productService.createProduct(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }
    
    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: String, @RequestBody product: Product): ResponseEntity<Any> {
        return try {
            val updatedProduct = productService.updateProduct(id, product)
            ResponseEntity.ok(updatedProduct)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Product not found")))
        }
    }
    
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            productService.deleteProduct(id)
            ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Product not found")))
        }
    }
}
