package com.example.skai.service

import com.example.skai.model.User
import com.example.skai.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {
    
    fun login(email: String, password: String): User {
        val userOpt = userRepository.findByEmail(email)
        if (userOpt.isPresent && userOpt.get().password == password) {
            return userOpt.get()
        }
        throw RuntimeException("Invalid email or password")
    }
    
    fun register(user: User): User {
        if (userRepository.findByEmail(user.email).isPresent) {
            throw RuntimeException("Email already exists")
        }
        val newUser = if (user.id.isNullOrEmpty()) {
            User(
                id = UUID.randomUUID().toString(),
                email = user.email,
                password = user.password,
                name = user.name,
                phone = user.phone,
                address = user.address,
                isAdmin = user.isAdmin,
                profileImageUri = user.profileImageUri
            )
        } else {
            user
        }
        return userRepository.save(newUser)
    }
    
    fun getUserById(id: String): User {
        return userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
    }
    
    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("User not found") }
    }
    
    fun updateUser(id: String, user: User): User {
        val existingUser = getUserById(id)
        val updatedUser = User(
            id = existingUser.id,
            email = existingUser.email,
            password = if (user.password.isNotEmpty()) user.password else existingUser.password,
            name = user.name,
            phone = user.phone,
            address = user.address,
            isAdmin = existingUser.isAdmin,
            profileImageUri = user.profileImageUri
        )
        return userRepository.save(updatedUser)
    }
    
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}

