package com.example.skai.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skai.data.model.Product
import com.example.skai.ui.screens.catalog.CatalogScreen
import com.example.skai.ui.viewmodel.AuthViewModel
import com.example.skai.ui.viewmodel.CartViewModel
import com.example.skai.ui.viewmodel.ProductViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de UI con JUnit4 y ComposeTestRule para CatalogScreen
 * Nota: Para JUnit5 puro, se necesitaría usar createComposeRule() de ui-test-junit5
 */
@RunWith(AndroidJUnit4::class)
class CatalogScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun catalogScreen_displaysProducts() {
        // Arrange
        val mockProducts = listOf(
            Product(
                id = "1",
                name = "Producto Test",
                description = "Descripción test",
                price = 15990.0,
                category = "Ropa",
                sizes = listOf("M", "L"),
                images = listOf("https://example.com/image.jpg"),
                stock = 10
            )
        )

        val productViewModel = mockk<ProductViewModel>(relaxed = true)
        val cartViewModel = mockk<CartViewModel>(relaxed = true)
        val authViewModel = mockk<AuthViewModel>(relaxed = true)

        every { productViewModel.filteredProducts } returns MutableStateFlow(mockProducts)
        every { productViewModel.selectedCategory } returns MutableStateFlow(null)
        every { productViewModel.searchQuery } returns MutableStateFlow("")
        every { productViewModel.isLoading } returns MutableStateFlow(false)
        every { productViewModel.getCategories() } returns listOf("Ropa", "Calzado")
        every { authViewModel.currentUser } returns MutableStateFlow(null)

        // Act
        composeTestRule.setContent {
            CatalogScreen(
                onNavigateToProductDetail = {},
                onNavigateToCart = {},
                onNavigateToOrders = {},
                onNavigateToAdmin = {},
                onNavigateToProfile = {},
                onLogout = {},
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Producto Test").assertExists()
        composeTestRule.onNodeWithText("SKAI").assertExists()
    }

    @Test
    fun catalogScreen_displaysSearchBar() {
        // Arrange
        val productViewModel = mockk<ProductViewModel>(relaxed = true)
        val cartViewModel = mockk<CartViewModel>(relaxed = true)
        val authViewModel = mockk<AuthViewModel>(relaxed = true)

        every { productViewModel.filteredProducts } returns MutableStateFlow(emptyList())
        every { productViewModel.selectedCategory } returns MutableStateFlow(null)
        every { productViewModel.searchQuery } returns MutableStateFlow("")
        every { productViewModel.isLoading } returns MutableStateFlow(false)
        every { productViewModel.getCategories() } returns emptyList()
        every { authViewModel.currentUser } returns MutableStateFlow(null)

        // Act
        composeTestRule.setContent {
            CatalogScreen(
                onNavigateToProductDetail = {},
                onNavigateToCart = {},
                onNavigateToOrders = {},
                onNavigateToAdmin = {},
                onNavigateToProfile = {},
                onLogout = {},
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Buscar productos...").assertExists()
    }
}

