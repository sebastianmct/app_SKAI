package com.example.skai.controller

import com.example.skai.model.User
import com.example.skai.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Map<String, String>): ResponseEntity<Any> {
        return try {
            val email = loginRequest["email"] ?: throw IllegalArgumentException("Email is required")
            val password = loginRequest["password"] ?: throw IllegalArgumentException("Password is required")
            val user = userService.login(email, password)
            ResponseEntity.ok(user)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf<String, String>("error" to (e.message ?: "Login failed")))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf<String, String>("error" to (e.message ?: "Invalid request")))
        }
    }
    
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<Any> {
        return try {
            val createdUser = userService.register(user)
            ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf<String, String>("error" to (e.message ?: "Registration failed")))
        }
    }
    
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val user = userService.getUserById(id)
            ResponseEntity.ok(user)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "User not found")))
        }
    }
    
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody user: User): ResponseEntity<Any> {
        return try {
            val updatedUser = userService.updateUser(id, user)
            ResponseEntity.ok(updatedUser)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "User not found")))
        }
    }
    
    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<Any> {
        return try {
            val user = userService.getUserByEmail(email)
            ResponseEntity.ok(user)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf<String, String>("error" to (e.message ?: "User not found")))
        }
    }
}

