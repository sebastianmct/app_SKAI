package com.example.skai.config

import com.example.skai.model.Product
import com.example.skai.model.User
import com.example.skai.repository.ProductRepository
import com.example.skai.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : CommandLineRunner {
    
    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)
    
    override fun run(vararg args: String?) {
        try {
            logger.info("Iniciando inicialización de datos en Oracle...")
            

            initializeUsers()
            

            initializeProducts()
            
            logger.info("Inicialización de datos completada exitosamente")
        } catch (e: Exception) {
            logger.error("Error durante la inicialización de datos: ${e.message}", e)
        }
    }
    
    private fun initializeUsers() {
        val userCount = userRepository.count()
        logger.info("Usuarios existentes en la base de datos: $userCount")
        
        if (userCount == 0L) {
            logger.info("Creando usuarios iniciales...")
            
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
            logger.info("Usuario administrador creado: ${admin.email}")
            
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
            logger.info("Usuario cliente creado: ${cliente.email}")
            
            println("✓ Usuarios de prueba inicializados en Oracle")
        } else {
            logger.info("Los usuarios ya existen en la base de datos, omitiendo inicialización")
        }
    }
    
    private fun initializeProducts() {
        val productCount = productRepository.count()
        logger.info("Productos existentes en la base de datos: $productCount")
        
        if (productCount == 0L) {
            logger.info("Creando productos iniciales...")
            
            val products = listOf(
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Camisa Casual Azul",
                    description = "Camisa de algodón 100% con corte casual perfecta para el día a día. Diseño moderno y cómodo.",
                    price = 29.0,
                    category = "Camisas",
                    sizes = listOf("S", "M", "L", "XL"),
                    images = listOf("https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500"),
                    stock = 50,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Pantalón Jeans Clásico",
                    description = "Jeans de mezclilla premium con corte clásico. Duradero y con estilo atemporal.",
                    price = 49.0,
                    category = "Pantalones",
                    sizes = listOf("28", "30", "32", "34", "36"),
                    images = listOf("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"),
                    stock = 30,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Vestido Elegante Negro",
                    description = "Vestido de noche elegante en color negro. Perfecto para ocasiones especiales.",
                    price = 79.0,
                    category = "Vestidos",
                    sizes = listOf("XS", "S", "M", "L"),
                    images = listOf("https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=500"),
                    stock = 25,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Zapatos Deportivos Blancos",
                    description = "Zapatos deportivos cómodos y modernos. Ideales para ejercicio y uso casual.",
                    price = 89.0,
                    category = "Zapatos",
                    sizes = listOf("6", "7", "8", "9", "10", "11"),
                    images = listOf("https://images.unsplash.com/photo-1549298916-b41d501d3772?w=500"),
                    stock = 40,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Cinturón de Cuero Marrón",
                    description = "Cinturón de cuero genuino en color marrón. Accesorio elegante y duradero.",
                    price = 24.0,
                    category = "Accesorios",
                    sizes = listOf("S", "M", "L"),
                    images = listOf("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500"),
                    stock = 60,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    name = "Chaqueta de Cuero Negra",
                    description = "Chaqueta de cuero sintético con estilo rebelde. Perfecta para el invierno.",
                    price = 129.0,
                    category = "Chaquetas",
                    sizes = listOf("S", "M", "L", "XL"),
                    images = listOf("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500"),
                    stock = 20,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
                )
            )
            
            products.forEach { product ->
                productRepository.save(product)
                logger.info("Producto creado: ${product.name} (${product.category})")
            }
            
            println("✓ ${products.size} productos inicializados en Oracle")
        } else {
            logger.info("Los productos ya existen en la base de datos, omitiendo inicialización")
        }
    }
}
