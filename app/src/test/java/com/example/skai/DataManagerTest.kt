package com.example.skai

import com.example.skai.data.model.CartItem
import com.example.skai.data.model.Order
import com.example.skai.data.model.OrderStatus
import com.example.skai.data.model.Product
import com.example.skai.data.model.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.UUID

class DataManagerTest : StringSpec({

    beforeTest {

        DataManager.initialize()
    }

    "Cuando inicializo DataManager, debe crear usuarios y productos por defecto" {

        val users = DataManager.getAllProducts()
        val products = DataManager.getAllProducts()
        

        products.isNotEmpty() shouldBe true
    }

    "Cuando hago login con credenciales correctas, debe retornar el usuario" {

        val email = "admin@skai.com"
        val password = "admin123"
        

        val user = DataManager.login(email, password)
        

        user shouldNotBe null
        user?.email shouldBe email
        user?.isAdmin shouldBe true
    }

    "Cuando hago login con credenciales incorrectas, debe retornar null" {

        val email = "admin@skai.com"
        val password = "password_incorrecta"
        

        val user = DataManager.login(email, password)
        

        user shouldBe null
    }

    "Cuando registro un nuevo usuario, debe agregarlo correctamente" {

        val newUser = User(
            id = UUID.randomUUID().toString(),
            email = "nuevo@skai.com",
            password = "password123",
            name = "Usuario Nuevo",
            phone = "+1234567892",
            address = "789 New Street",
            isAdmin = false
        )
        

        val result = DataManager.register(newUser)
        

        result shouldBe true
    }

    "Cuando registro un usuario con email existente, debe retornar false" {

        DataManager.initialize()
        val existingUser = User(
            id = UUID.randomUUID().toString(),
            email = "admin@skai.com",
            password = "password123",
            name = "Usuario Duplicado",
            phone = "+1234567893",
            address = "999 Duplicate Street",
            isAdmin = false
        )
        

        val result = DataManager.register(existingUser)
        

        result shouldBe false
    }

    "Cuando actualizo un usuario existente, debe modificar los datos" {

        DataManager.initialize()
        val user = DataManager.login("admin@skai.com", "admin123")!!
        val updatedUser = user.copy(name = "Admin Actualizado", phone = "+9999999999")
        

        val result = DataManager.updateUser(updatedUser)
        

        result shouldBe true
        val retrievedUser = DataManager.login("admin@skai.com", "admin123")
        retrievedUser?.name shouldBe "Admin Actualizado"
    }

    "Cuando obtengo todos los productos, debe retornar solo productos activos" {

        val inactiveProduct = Product(
            id = UUID.randomUUID().toString(),
            name = "Producto Inactivo",
            description = "Descripción",
            price = 10000.0,
            category = "Ropa",
            sizes = listOf("M"),
            images = listOf(),
            stock = 0,
            isActive = false
        )
        DataManager.addProduct(inactiveProduct)
        

        val products = DataManager.getAllProducts()
        

        products.none { it.id == inactiveProduct.id } shouldBe true
    }

    "Cuando busco un producto por ID, debe retornar el producto correcto" {

        val product = Product(
            id = "test-product-123",
            name = "Producto Test",
            description = "Descripción test",
            price = 19990.0,
            category = "Ropa",
            sizes = listOf("S", "M"),
            images = listOf(),
            stock = 10
        )
        DataManager.addProduct(product)
        

        val found = DataManager.getProductById("test-product-123")
        

        found shouldNotBe null
        found?.name shouldBe "Producto Test"
    }

    "Cuando busco productos por categoría, debe filtrar correctamente" {

        val ropaProduct = Product(
            id = UUID.randomUUID().toString(),
            name = "Camiseta",
            description = "Ropa",
            price = 19990.0,
            category = "Ropa",
            sizes = listOf("M"),
            images = listOf(),
            stock = 10
        )
        val zapatoProduct = Product(
            id = UUID.randomUUID().toString(),
            name = "Zapatos",
            description = "Calzado",
            price = 89990.0,
            category = "Calzado",
            sizes = listOf("40"),
            images = listOf(),
            stock = 5
        )
        DataManager.addProduct(ropaProduct)
        DataManager.addProduct(zapatoProduct)
        

        val ropaProducts = DataManager.getProductsByCategory("Ropa")
        

        ropaProducts.all { it.category == "Ropa" } shouldBe true
        ropaProducts.any { it.id == ropaProduct.id } shouldBe true
    }

    "Cuando busco productos por texto, debe encontrar coincidencias en nombre y descripción" {

        DataManager.initialize()

        DataManager.getAllProducts().forEach { DataManager.deleteProduct(it) }
        
        val product1 = Product(
            id = UUID.randomUUID().toString(),
            name = "Camiseta Azul",
            description = "Camiseta de algodón",
            price = 19990.0,
            category = "Ropa",
            sizes = listOf("M"),
            images = listOf(),
            stock = 10
        )
        val product2 = Product(
            id = UUID.randomUUID().toString(),
            name = "Pantalón",
            description = "Pantalón de algodón premium",
            price = 49990.0,
            category = "Pantalones",
            sizes = listOf("30"),
            images = listOf(),
            stock = 5
        )
        DataManager.addProduct(product1)
        DataManager.addProduct(product2)
        

        val results = DataManager.searchProducts("algodón")
        

        results.size shouldBe 2
        results.any { it.id == product1.id } shouldBe true
        results.any { it.id == product2.id } shouldBe true
    }

    "Cuando agrego un producto al carrito, debe guardarse correctamente" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        DataManager.setCurrentUser(user)
        
        val cartItem = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-123",
            productName = "Camiseta",
            productPrice = 19990.0,
            productImage = "",
            selectedSize = "M",
            quantity = 1
        )
        

        DataManager.addToCart(cartItem)
        

        val cartItems = DataManager.getCartItems(user.id)
        cartItems.size shouldBe 1
        cartItems.first().productName shouldBe "Camiseta"
    }

    "Cuando agrego el mismo producto dos veces, debe incrementar la cantidad" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        DataManager.setCurrentUser(user)
        

        DataManager.clearCart(user.id)
        
        val cartItem = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-123",
            productName = "Camiseta",
            productPrice = 19990.0,
            productImage = "",
            selectedSize = "M",
            quantity = 1
        )
        DataManager.addToCart(cartItem)
        

        DataManager.addToCart(cartItem)
        

        val cartItems = DataManager.getCartItems(user.id)
        cartItems.size shouldBe 1
        cartItems.first().quantity shouldBe 2
    }

    "Cuando actualizo la cantidad de un item del carrito, debe reflejarse el cambio" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        DataManager.setCurrentUser(user)
        
        val cartItem = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-123",
            productName = "Camiseta",
            productPrice = 19990.0,
            productImage = "",
            selectedSize = "M",
            quantity = 1
        )
        DataManager.addToCart(cartItem)
        

        DataManager.updateCartItemQuantity(user.id, "prod-123", "M", 3)
        

        val cartItems = DataManager.getCartItems(user.id)
        cartItems.first().quantity shouldBe 3
    }

    "Cuando actualizo la cantidad a 0, debe eliminar el item del carrito" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        DataManager.setCurrentUser(user)
        
        val cartItem = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-123",
            productName = "Camiseta",
            productPrice = 19990.0,
            productImage = "",
            selectedSize = "M",
            quantity = 1
        )
        DataManager.addToCart(cartItem)
        

        DataManager.updateCartItemQuantity(user.id, "prod-123", "M", 0)
        

        val cartItems = DataManager.getCartItems(user.id)
        cartItems.isEmpty() shouldBe true
    }

    "Cuando calculo el total del carrito, debe sumar correctamente" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        DataManager.setCurrentUser(user)
        
        val item1 = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-1",
            productName = "Producto 1",
            productPrice = 19990.0,
            productImage = "",
            selectedSize = "M",
            quantity = 2
        )
        val item2 = CartItem(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            productId = "prod-2",
            productName = "Producto 2",
            productPrice = 49990.0,
            productImage = "",
            selectedSize = "L",
            quantity = 1
        )
        DataManager.addToCart(item1)
        DataManager.addToCart(item2)
        

        val total = DataManager.getCartTotal(user.id)
        

        total shouldBe 89970.0
    }

    "Cuando creo un pedido, debe guardarse correctamente" {

        DataManager.initialize()
        val user = DataManager.login("cliente@skai.com", "cliente123")!!
        
        val order = Order(
            id = "order-123",
            userId = user.id,
            items = emptyList(),
            totalAmount = 89970.0,
            status = OrderStatus.PENDING,
            shippingAddress = user.address,
            notes = "Notas del pedido"
        )
        

        DataManager.createOrder(order)
        

        val foundOrder = DataManager.getOrderById("order-123")
        foundOrder shouldNotBe null
        foundOrder?.totalAmount shouldBe 89970.0
    }

    "Cuando obtengo las categorías, debe retornar todas las categorías únicas" {

        val product1 = Product(
            id = UUID.randomUUID().toString(),
            name = "Producto 1",
            description = "Desc",
            price = 10000.0,
            category = "Ropa",
            sizes = listOf("M"),
            images = listOf(),
            stock = 10
        )
        val product2 = Product(
            id = UUID.randomUUID().toString(),
            name = "Producto 2",
            description = "Desc",
            price = 20000.0,
            category = "Calzado",
            sizes = listOf("40"),
            images = listOf(),
            stock = 5
        )
        val product3 = Product(
            id = UUID.randomUUID().toString(),
            name = "Producto 3",
            description = "Desc",
            price = 30000.0,
            category = "Ropa",
            sizes = listOf("L"),
            images = listOf(),
            stock = 8
        )
        DataManager.addProduct(product1)
        DataManager.addProduct(product2)
        DataManager.addProduct(product3)
        

        val categories = DataManager.getCategories()
        

        categories.contains("Ropa") shouldBe true
        categories.contains("Calzado") shouldBe true
        categories.size shouldBe categories.distinct().size
    }
})

