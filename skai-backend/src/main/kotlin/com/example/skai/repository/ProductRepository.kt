package com.example.skai.repository

import com.example.skai.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, String> {
    fun findByCategory(category: String): List<Product>
}

