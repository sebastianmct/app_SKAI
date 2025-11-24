package com.example.skai

import com.example.skai.data.model.User
import com.example.skai.data.model.Product
import com.example.skai.data.model.CartItem
import com.example.skai.data.model.Order
import java.util.UUID

object DataManager {

    private val users = mutableListOf<User>()
    private val products = mutableListOf<Product>()
    private val cartItems = mutableListOf<CartItem>()
    private val orders = mutableListOf<Order>()

    var currentUser: User? = null
        private set

    fun initialize() {

        users.addAll(listOf(
            User(
                id = UUID.randomUUID().toString(),
                email = "admin@skai.com",
                password = "admin123",
                name = "Administrador SKAI",
                phone = "+1234567890",
                address = "1236 Admin Street, City",
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
        ))


        products.addAll(listOf(
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
            )
        ))
    }


    fun login(email: String, password: String): User? {
        return users.find { it.email == email && it.password == password }
    }

    fun register(user: User): Boolean {
        val existingUser = users.find { it.email == user.email }
        return if (existingUser == null) {
            users.add(user)
            true
        } else {
            false
        }
    }

    fun setCurrentUser(user: User?) {
        currentUser = user
    }

    fun updateUser(user: User): Boolean {
        val index = users.indexOfFirst { it.id == user.id }
        return if (index != -1) {
            users[index] = user
            if (currentUser?.id == user.id) {
                currentUser = user
            }
            true
        } else {
            false
        }
    }


    fun getAllProducts(): List<Product> = products.filter { it.isActive }

    fun getProductById(id: String): Product? = products.find { it.id == id }

    fun getProductsByCategory(category: String): List<Product> {
        return products.filter { it.category == category && it.isActive }
    }

    fun searchProducts(query: String): List<Product> {
        return products.filter {
            it.isActive && (
                    it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                    )
        }
    }

    fun addProduct(product: Product) {
        products.add(product)
    }

    fun updateProduct(product: Product) {
        val index = products.indexOfFirst { it.id == product.id }
        if (index != -1) {
            products[index] = product
        }
    }

    fun deleteProduct(product: Product) {
        products.removeAll { it.id == product.id }
    }

    fun getCategories(): List<String> {
        return products.map { it.category }.distinct()
    }


    fun getCartItems(userId: String): List<CartItem> {
        return cartItems.filter { it.userId == userId }
    }

    fun addToCart(cartItem: CartItem) {
        val existingItem = cartItems.find {
            it.userId == cartItem.userId &&
                    it.productId == cartItem.productId &&
                    it.selectedSize == cartItem.selectedSize
        }

        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
        } else {
            cartItems.add(cartItem)
        }
    }

    fun updateCartItemQuantity(userId: String, productId: String, size: String, quantity: Int) {
        val item = cartItems.find {
            it.userId == userId &&
                    it.productId == productId &&
                    it.selectedSize == size
        }

        if (item != null) {
            if (quantity <= 0) {
                cartItems.remove(item)
            } else {
                val index = cartItems.indexOf(item)
                cartItems[index] = item.copy(quantity = quantity)
            }
        }
    }

    fun removeFromCart(userId: String, productId: String, size: String) {
        cartItems.removeAll {
            it.userId == userId &&
                    it.productId == productId &&
                    it.selectedSize == size
        }
    }

    fun clearCart(userId: String) {
        cartItems.removeAll { it.userId == userId }
    }

    fun getCartItemCount(userId: String): Int {
        return cartItems.filter { it.userId == userId }.sumOf { it.quantity }
    }

    fun getCartTotal(userId: String): Double {
        return cartItems.filter { it.userId == userId }.sumOf { it.productPrice * it.quantity }
    }


    fun getOrders(userId: String): List<Order> {
        return orders.filter { it.userId == userId }.sortedByDescending { it.createdAt }
    }

    fun createOrder(order: Order) {
        orders.add(order)
    }

    fun getOrderById(orderId: String): Order? {
        return orders.find { it.id == orderId }
    }
}
