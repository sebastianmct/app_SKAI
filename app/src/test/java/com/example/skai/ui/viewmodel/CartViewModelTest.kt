package com.example.skai.ui.viewmodel

import com.example.skai.DataManager
import com.example.skai.data.model.CartItem
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.ExperimentalCoroutinesApi;

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.util.UUID


@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        mockkObject(DataManager)
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkAll()
    }

    "Cuando cargo items del carrito, debe obtenerlos desde DataManager" {
        runTest(testDispatcher) {

            val userId = "user-123"
            val cartItems = listOf(
                CartItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    productId = "prod-1",
                    productName = "Camiseta",
                    productPrice = 19990.0,
                    productImage = "",
                    selectedSize = "M",
                    quantity = 2
                )
            )
            
            every { DataManager.getCartItems(userId) } returns cartItems
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.loadCartItems(userId)
            advanceUntilIdle()
            

            val loadedItems = viewModel.cartItems.value
            loadedItems.size shouldBe 1
            loadedItems.first().productName shouldBe "Camiseta"
        }
    }

    "Cuando agrego un item al carrito, debe guardarse correctamente" {
        runTest(testDispatcher) {

            val userId = "user-123"
            val cartItem = CartItem(
                id = UUID.randomUUID().toString(),
                userId = userId,
                productId = "prod-1",
                productName = "Camiseta",
                productPrice = 19990.0,
                productImage = "",
                selectedSize = "M",
                quantity = 1
            )
            
            every { DataManager.addToCart(any()) } just Runs
            every { DataManager.getCartItems(userId) } returns listOf(cartItem)
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.addToCart(cartItem)
            advanceUntilIdle()
            

            coVerify(exactly = 1) { DataManager.addToCart(cartItem) }
        }
    }

    "Cuando actualizo la cantidad, debe reflejarse el cambio" {
        runTest(testDispatcher) {

            val userId = "user-123"
            val cartItem = CartItem(
                id = UUID.randomUUID().toString(),
                userId = userId,
                productId = "prod-1",
                productName = "Camiseta",
                productPrice = 19990.0,
                productImage = "",
                selectedSize = "M",
                quantity = 1
            )
            
            every { DataManager.updateCartItemQuantity(any(), any(), any(), any()) } just Runs
            every { DataManager.getCartItems(userId) } returns listOf(cartItem.copy(quantity = 3))
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.updateQuantity(userId, "prod-1", "M", 3)
            advanceUntilIdle()
            

            coVerify(exactly = 1) { DataManager.updateCartItemQuantity(userId, "prod-1", "M", 3) }
        }
    }

    "Cuando elimino un item del carrito, debe quitarse" {
        runTest(testDispatcher) {

            val userId = "user-123"
            
            every { DataManager.removeFromCart(any(), any(), any()) } just Runs
            every { DataManager.getCartItems(userId) } returns emptyList()
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.removeFromCart(userId, "prod-1", "M")
            advanceUntilIdle()
            

            coVerify(exactly = 1) { DataManager.removeFromCart(userId, "prod-1", "M") }
        }
    }

    "Cuando calculo el total, debe sumar correctamente" {
        runTest(testDispatcher) {

            val userId = "user-123"
            val cartItems = listOf(
                CartItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    productId = "prod-1",
                    productName = "Producto 1",
                    productPrice = 19990.0,
                    productImage = "",
                    selectedSize = "M",
                    quantity = 2
                ),
                CartItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    productId = "prod-2",
                    productName = "Producto 2",
                    productPrice = 49990.0,
                    productImage = "",
                    selectedSize = "L",
                    quantity = 1
                )
            )
            
            every { DataManager.getCartItems(userId) } returns cartItems
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.loadCartItems(userId)
            advanceUntilIdle()
            

            val total = viewModel.totalAmount.value
            total shouldBe 89970.0
        }
    }

    "Cuando limpio el carrito, debe quedar vacío" {
        runTest(testDispatcher) {

            val userId = "user-123"
            
            every { DataManager.clearCart(any()) } just Runs
            every { DataManager.getCartItems(userId) } returns emptyList()
            

            val viewModel = CartViewModel()
            advanceUntilIdle()
            viewModel.clearCart(userId)
            advanceUntilIdle()
            

            val items = viewModel.cartItems.first()
            items.isEmpty() shouldBe true
        }
    }
})

