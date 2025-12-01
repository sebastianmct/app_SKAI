package com.example.skai.config

import com.example.skai.model.Product
import com.example.skai.model.User
import com.example.skai.repository.ProductRepository
import com.example.skai.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {

        if (userRepository.count() == 0L) {
            val admin = User(
                id = UUID.randomUUID().toString(),
                email = "admin@skai.com",
                password = "admin123",
                name = "Administrador SKAI",
                phone = "+1234567890",
                address = "123 Admin Street, City",
                isAdmin = true
            )
            userRepository.save(admin)
            
            val cliente = User(
                id = UUID.randomUUID().toString(),
                email = "cliente@skai.com",
                password = "cliente123",
                name = "Cliente SKAI",
                phone = "+1234567891",
                address = "456 Client Avenue, City",
                isAdmin = false
            )
            userRepository.save(cliente)
            
            println("Usuarios de prueba inicializados")
        }
        

        if (productRepository.count() == 0L) {
            val product1 = Product(
                id = UUID.randomUUID().toString(),
                name = "Camisa Casual Azul",
                description = "Camisa de algodón 100% con corte casual perfecta para el día a día. Diseño moderno y cómodo.",
                price = 29990.0,
                category = "Camisas",
                sizes = listOf("S", "M", "L", "XL"),
                images = listOf("https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500"),
                stock = 50,
                isActive = true,
                createdAt = System.currentTimeMillis()
            )
            productRepository.save(product1)
            
            val product2 = Product(
                id = UUID.randomUUID().toString(),
                name = "Pantalón Jeans Clásico",
                description = "Jeans de mezclilla premium con corte clásico. Duradero y con estilo atemporal.",
                price = 49990.0,
                category = "Pantalones",
                sizes = listOf("28", "30", "32", "34", "36"),
                images = listOf("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"),
                stock = 30,
                isActive = true,
                createdAt = System.currentTimeMillis()
            )
            productRepository.save(product2)
            
            val product3 = Product(
                id = UUID.randomUUID().toString(),
                name = "Vestido Elegante Negro",
                description = "Vestido de noche elegante en color negro. Perfecto para ocasiones especiales.",
                price = 79990.0,
                category = "Vestidos",
                sizes = listOf("XS", "S", "M", "L"),
                images = listOf("https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=500"),
                stock = 25,
                isActive = true,
                createdAt = System.currentTimeMillis()
            )
            productRepository.save(product3)
            
            println("Productos propios inicializados")
        }
    }
}
