package com.example.skai.controller

import com.example.skai.model.Order
import com.example.skai.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    
    @GetMapping("/user/{userId}")
    fun getOrdersByUserId(@PathVariable userId: String): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId))
    }
    
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val order = orderService.getOrderById(id)
            ResponseEntity.ok(order)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Order not found")))
        }
    }
    
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getAllOrders())
    }
    
    @PostMapping
    fun createOrder(@RequestBody order: Order): ResponseEntity<Order> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder)
    }
    
    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: String, @RequestBody order: Order): ResponseEntity<Any> {
        return try {
            val updatedOrder = orderService.updateOrder(id, order)
            ResponseEntity.ok(updatedOrder)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Order not found")))
        }
    }
    
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            orderService.deleteOrder(id)
            ResponseEntity.ok().build()
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "Order not found")))
        }
    }
}

