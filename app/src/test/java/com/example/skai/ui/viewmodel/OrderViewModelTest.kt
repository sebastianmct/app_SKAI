package com.example.skai.ui.viewmodel

import com.example.skai.DataManager
import com.example.skai.data.model.CartItem
import com.example.skai.data.model.Order
import com.example.skai.data.model.OrderStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
class OrderViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        mockkObject(DataManager)
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkAll()
    }

    "Cuando cargo pedidos, debe obtenerlos desde DataManager" {
        runTest(testDispatcher) {

            val userId = "user-123"
            val orders = listOf(
                Order(
                    id = "order-1",
                    userId = userId,
                    items = emptyList(),
                    totalAmount = 89970.0,
                    status = OrderStatus.PENDING,
                    shippingAddress = "Test Address",
                    notes = ""
                )
            )
            
            every { DataManager.getOrders(userId) } returns orders
            

            val viewModel = OrderViewModel()
            advanceUntilIdle()
            viewModel.loadOrders(userId)
            advanceUntilIdle()
            

            val loadedOrders = viewModel.orders.value
            loadedOrders.size shouldBe 1
            loadedOrders.first().id shouldBe "order-1"
        }
    }

    "Cuando creo un pedido, debe guardarse correctamente" {
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
            
            clearAllMocks()
            every { DataManager.createOrder(any()) } just Runs
            

            val viewModel = OrderViewModel()
            advanceUntilIdle()
            viewModel.createOrder(
                userId = userId,
                cartItems = cartItems,
                shippingAddress = "Test Address",
                notes = "Notas del pedido"
            )
            advanceUntilIdle()
            

            val orderCreated = viewModel.orderCreated.value
            orderCreated shouldBe true
            coVerify(exactly = 1) { DataManager.createOrder(any()) }
        }
    }

    "Cuando creo un pedido, debe calcular el total correctamente" {
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
            
            clearAllMocks()
            var capturedOrder: Order? = null
            every { DataManager.createOrder(any()) } answers {
                capturedOrder = firstArg()
            }
            

            val viewModel = OrderViewModel()
            advanceUntilIdle()
            viewModel.createOrder(
                userId = userId,
                cartItems = cartItems,
                shippingAddress = "Test Address",
                notes = ""
            )
            advanceUntilIdle()
            

            coVerify(exactly = 1) { DataManager.createOrder(any()) }
            capturedOrder shouldNotBe null
            capturedOrder?.totalAmount shouldBe 89970.0
        }
    }

    "Cuando reseteo el estado de pedido creado, debe volver a false" {
        runTest(testDispatcher) {

            val viewModel = OrderViewModel()
            advanceUntilIdle()
            

            every { DataManager.createOrder(any()) } just Runs
            viewModel.createOrder(
                userId = "user-123",
                cartItems = emptyList(),
                shippingAddress = "Test",
                notes = ""
            )
            advanceUntilIdle()
            testDispatcher.scheduler.advanceUntilIdle()
            

            viewModel.resetOrderCreated()
            advanceUntilIdle()
            

            val orderCreated = viewModel.orderCreated.first()
            orderCreated shouldBe false
        }
    }
})

