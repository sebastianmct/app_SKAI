package com.example.skai.ui.viewmodel

import com.example.skai.DataManager
import com.example.skai.data.model.Product
import com.example.skai.data.repository.ExternalProductRepository
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


@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        mockkObject(DataManager)
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkAll()
    }

    "Cuando cargo productos, debe obtenerlos desde DataManager" {
        runTest(testDispatcher) {

            val products = listOf(
                Product(
                    id = "1",
                    name = "Camiseta",
                    description = "Ropa básica",
                    price = 19990.0,
                    category = "Ropa",
                    sizes = listOf("M"),
                    images = listOf(),
                    stock = 10
                )
            )
            
            every { DataManager.getAllProducts() } returns products
            every { DataManager.getCategories() } returns listOf("Ropa")
            

            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns emptyList()
            
            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            

            val loadedProducts = viewModel.products.value
            loadedProducts.size shouldBe 1
        }
    }

    "Cuando filtro por categoría, debe mostrar solo productos de esa categoría" {
        runTest(testDispatcher) {
            val products = listOf(
                Product(
                    id = "1",
                    name = "Camiseta",
                    description = "Ropa",
                    price = 19990.0,
                    category = "Ropa",
                    sizes = listOf("M"),
                    images = listOf(),
                    stock = 10
                ),
                Product(
                    id = "2",
                    name = "Zapatos",
                    description = "Calzado",
                    price = 89990.0,
                    category = "Calzado",
                    sizes = listOf("40"),
                    images = listOf(),
                    stock = 5
                )
            )

            every { DataManager.getAllProducts() } returns products
            every { DataManager.getCategories() } returns listOf("Ropa", "Calzado")

            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns emptyList()
            

            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            viewModel.setCategory("Ropa")
            advanceUntilIdle()
            

            val filtered = viewModel.filteredProducts.value
            filtered.all { it.category == "Ropa" } shouldBe true
            filtered.size shouldBe 1
        }
    }

    "Cuando busco productos, debe filtrar por nombre y descripción" {
        runTest(testDispatcher) {

            val products = listOf(
                Product(
                    id = "1",
                    name = "Camiseta Azul",
                    description = "Camiseta de algodón",
                    price = 19990.0,
                    category = "Ropa",
                    sizes = listOf("M"),
                    images = listOf(),
                    stock = 10
                ),
                Product(
                    id = "2",
                    name = "Pantalón",
                    description = "Pantalón de mezclilla",
                    price = 49990.0,
                    category = "Pantalones",
                    sizes = listOf("30"),
                    images = listOf(),
                    stock = 5
                )
            )

            every { DataManager.getAllProducts() } returns products
            every { DataManager.getCategories() } returns listOf("Ropa", "Pantalones")
            
            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns emptyList()


            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            viewModel.setSearchQuery("Camiseta")
            advanceUntilIdle()
            

            val filtered = viewModel.filteredProducts.value
            filtered.size shouldBe 1
            filtered.first().name shouldBe "Camiseta Azul"
        }
    }

    "Cuando obtengo categorías, debe retornar todas las categorías disponibles" {
        runTest(testDispatcher) {

            val categories = listOf("Ropa", "Pantalones", "Calzado", "Accesorios")
            
            every { DataManager.getAllProducts() } returns emptyList()
            every { DataManager.getCategories() } returns categories
            
            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns emptyList()

            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            val result = viewModel.getCategories()
            

            result shouldBe categories
        }
    }

    "Cuando cargo productos externos, debe combinarlos con los locales" {
        runTest(testDispatcher) {

            val localProducts = listOf(
                Product(
                    id = "local-1",
                    name = "Producto Local",
                    description = "Local",
                    price = 19990.0,
                    category = "Ropa",
                    sizes = listOf("M"),
                    images = listOf(),
                    stock = 10
                )
            )
            val externalProducts = listOf(
                Product(
                    id = "external-1",
                    name = "Producto Externo",
                    description = "Externo",
                    price = 29990.0,
                    category = "Ropa",
                    sizes = listOf("L"),
                    images = listOf(),
                    stock = 5
                )
            )
            
            every { DataManager.getAllProducts() } returns localProducts
            every { DataManager.getCategories() } returns listOf("Ropa")
            
            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns externalProducts
            

            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            

            val allProducts = viewModel.filteredProducts.value
            allProducts.size shouldBe 2
            allProducts.any { it.id == "local-1" } shouldBe true
            allProducts.any { it.id == "external-1" } shouldBe true
        }
    }

    "Cuando cambio la búsqueda, debe actualizar los resultados filtrados" {
        runTest(testDispatcher) {

            val products = listOf(
                Product(
                    id = "1",
                    name = "Camiseta Blanca",
                    description = "Camiseta básica",
                    price = 19990.0,
                    category = "Ropa",
                    sizes = listOf("M"),
                    images = listOf(),
                    stock = 10
                ),
                Product(
                    id = "2",
                    name = "Pantalón Negro",
                    description = "Pantalón elegante",
                    price = 49990.0,
                    category = "Pantalones",
                    sizes = listOf("30"),
                    images = listOf(),
                    stock = 5
                )
            )
            
            every { DataManager.getAllProducts() } returns products
            every { DataManager.getCategories() } returns listOf("Ropa", "Pantalones")
            
            val externalRepo = mockk<ExternalProductRepository>(relaxed = true)
            coEvery { externalRepo.getAllExternalProducts() } returns emptyList()
            

            val viewModel = ProductViewModel(externalRepo)
            advanceUntilIdle()
            viewModel.setSearchQuery("Negro")
            advanceUntilIdle()
            

            val resultados = viewModel.filteredProducts.value
            resultados.size shouldBe 1
            resultados.first().name shouldBe "Pantalón Negro"
        }
    }
})
