package com.example.skai.repository

import com.example.skai.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, String> {
    fun findByUserId(userId: String): List<Order>
}

