package com.example.skai.ui.viewmodel

import com.example.skai.DataManager
import com.example.skai.data.model.User
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
class AuthViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        mockkObject(DataManager)
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkAll()
    }

    "Cuando hago login con credenciales válidas, debe establecer el usuario actual" {
        runTest(testDispatcher) {

            val user = User(
                id = "user-123",
                email = "test@skai.com",
                password = "password123",
                name = "Usuario Test",
                phone = "+1234567890",
                address = "Test Address",
                isAdmin = false
            )
            
            every { DataManager.login("test@skai.com", "password123") } returns user
            every { DataManager.setCurrentUser(any()) } just Runs
            every { DataManager.currentUser } returns user
            

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.login("test@skai.com", "password123")
            advanceUntilIdle()
            

            val currentUser = viewModel.currentUser.first()
            currentUser shouldNotBe null
            currentUser?.email shouldBe "test@skai.com"
        }
    }

    "Cuando hago login con credenciales inválidas, debe mostrar error" {
        runTest(testDispatcher) {

            every { DataManager.login("test@skai.com", "wrong") } returns null
            every { DataManager.currentUser } returns null
            every { DataManager.setCurrentUser(any()) } just Runs
            

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.login("test@skai.com", "wrong")
            advanceUntilIdle()


            val error = viewModel.errorMessage.value
            error shouldNotBe null
            error?.contains("inválidas") shouldBe true
        }
    }

    "Cuando registro un nuevo usuario, debe establecerlo como usuario actual" {
        runTest(testDispatcher) {

            val newUser = User(
                id = UUID.randomUUID().toString(),
                email = "nuevo@skai.com",
                password = "password123",
                name = "Usuario Nuevo",
                phone = "+1234567891",
                address = "New Address",
                isAdmin = false
            )
            
            every { DataManager.register(any()) } returns true
            every { DataManager.setCurrentUser(any()) } just Runs
            every { DataManager.currentUser } returns newUser
            

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.register(newUser)
            advanceUntilIdle()


            val currentUser = viewModel.currentUser.first()
            currentUser shouldNotBe null
            currentUser?.email shouldBe "nuevo@skai.com"
        }
    }

    "Cuando registro un usuario con email existente, debe mostrar error" {
        runTest(testDispatcher) {

            val existingUser = User(
                id = UUID.randomUUID().toString(),
                email = "existente@skai.com",
                password = "password123",
                name = "Usuario Existente",
                phone = "+1234567892",
                address = "Existing Address",
                isAdmin = false
            )
            
            every { DataManager.register(any()) } returns false
            every { DataManager.currentUser } returns null
            every { DataManager.setCurrentUser(any()) } just Runs
            

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.register(existingUser)
            advanceUntilIdle()
            

            val error = viewModel.errorMessage.value
            error shouldNotBe null
            error?.contains("ya está registrado") shouldBe true
        }
    }

    "Cuando hago logout, debe limpiar el usuario actual" {
        runTest(testDispatcher) {

            val user = User(
                id = "user-123",
                email = "test@skai.com",
                password = "password123",
                name = "Usuario Test",
                phone = "+1234567890",
                address = "Test Address",
                isAdmin = false
            )
            
            every { DataManager.currentUser } returns user andThen null
            every { DataManager.setCurrentUser(null) } just Runs
            

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.logout()
            advanceUntilIdle()
            

            val currentUser = viewModel.currentUser.first()
            currentUser shouldBe null
        }
    }

    "Cuando actualizo un usuario, debe reflejar los cambios" {
        runTest(testDispatcher) {

            val user = User(
                id = "user-123",
                email = "test@skai.com",
                password = "password123",
                name = "Usuario Original",
                phone = "+1234567890",
                address = "Original Address",
                isAdmin = false
            )
            val updatedUser = user.copy(name = "Usuario Actualizado", phone = "+9999999999")
            
            every { DataManager.currentUser } returns user
            every { DataManager.updateUser(any()) } returns true
            every { DataManager.setCurrentUser(any()) } just Runs

            val viewModel = AuthViewModel()
            advanceUntilIdle()
            viewModel.updateUser(updatedUser)
            advanceUntilIdle()
            

            coVerify(exactly = 1) { DataManager.updateUser(updatedUser) }
        }
    }
})

