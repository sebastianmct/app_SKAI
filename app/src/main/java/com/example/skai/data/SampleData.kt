package com.example.skai.data

import com.example.skai.data.model.Product
import com.example.skai.data.model.User
import java.util.UUID

object SampleData {
    
    val sampleUsers = listOf(
        User(
            id = UUID.randomUUID().toString(),
            email = "admin@skai.com",
            password = "admin123",
            name = "Administrador SKAI",
            phone = "+1234567890",
            address = "123 Admin Street, City",
            isAdmin = true
        ),
        User(
            id = UUID.randomUUID().toString(),
            email = "cliente@skai.com",
            password = "cliente123",
            name = "Cliente SKAI",
            phone = "+1234567891",
            address = "456 Client Avenue, City",
            isAdmin = false
        )
    )

    val sampleProducts = listOf(
        Product(
            id = UUID.randomUUID().toString(),
            name = "Camisa Casual Azul",
            description = "Camisa de algodón 100% con corte casual perfecta para el día a día. Diseño moderno y cómodo.",
            price = 29.990,
            category = "Camisas",
            sizes = listOf("S", "M", "L", "XL"),
            images = listOf("https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500"),
            stock = 50,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Pantalón Jeans Clásico",
            description = "Jeans de mezclilla premium con corte clásico. Duradero y con estilo atemporal.",
            price = 49.990,
            category = "Pantalones",
            sizes = listOf("28", "30", "32", "34", "36"),
            images = listOf("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"),
            stock = 30,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Vestido Elegante Negro",
            description = "Vestido de noche elegante en color negro. Perfecto para ocasiones especiales.",
            price = 79.990,
            category = "Vestidos",
            sizes = listOf("XS", "S", "M", "L"),
            images = listOf("https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=500"),
            stock = 25,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Zapatos Deportivos Blancos",
            description = "Zapatos deportivos cómodos y modernos. Ideales para ejercicio y uso casual.",
            price = 89.990,
            category = "Zapatos",
            sizes = listOf("6", "7", "8", "9", "10", "11"),
            images = listOf("https://images.unsplash.com/photo-1549298916-b41d501d3772?w=500"),
            stock = 40,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Cinturón de Cuero Marrón",
            description = "Cinturón de cuero genuino en color marrón. Accesorio elegante y duradero.",
            price = 24.990,
            category = "Accesorios",
            sizes = listOf("S", "M", "L"),
            images = listOf("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500"),
            stock = 60,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Chaqueta de Cuero Negra",
            description = "Chaqueta de cuero sintético con estilo rebelde. Perfecta para el invierno.",
            price = 129.990,
            category = "Chaquetas",
            sizes = listOf("S", "M", "L", "XL"),
            images = listOf("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500"),
            stock = 20,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Sudadera con Capucha Gris",
            description = "Sudadera cómoda con capucha. Ideal para días relajados y clima fresco.",
            price = 39.990,
            category = "Sudaderas",
            sizes = listOf("S", "M", "L", "XL"),
            images = listOf("https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=500"),
            stock = 35,
            isActive = true
        ),
        Product(
            id = UUID.randomUUID().toString(),
            name = "Falda Plisada Azul",
            description = "Falda plisada elegante en color azul marino. Perfecta para oficina o eventos.",
            price = 34.990,
            category = "Faldas",
            sizes = listOf("XS", "S", "M", "L"),
            images = listOf("https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=500"),
            stock = 28,
            isActive = true
        )
    )
}
