package com.example.skai.data.repository

import com.example.skai.data.api.ExternalProduct
import com.example.skai.data.api.ExternalProductApiService
import com.example.skai.data.api.Rating
import com.example.skai.data.model.Product
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runTest
import retrofit2.Response

class ExternalProductRepositoryTest : StringSpec({

    lateinit var externalApiService: ExternalProductApiService
    lateinit var repository: ExternalProductRepository

    beforeTest {

        externalApiService = mockk(relaxed = true)
        repository = ExternalProductRepository(externalApiService)
    }

    afterTest {
        clearAllMocks()
    }


    "Cuando la API externa falla, debe retornar una lista vacía" {
        runTest {


            coEvery { externalApiService.getAllProducts() } throws Exception("Sin conexión")


            val result = repository.getAllExternalProducts()


            result shouldBe emptyList()
        }
    }

    "Cuando obtengo productos por categoría, debe filtrar y convertir correctamente" {
        runTest {

            val categoryProducts = listOf(
                ExternalProduct(
                    id = 2,
                    title = "Mens Casual T-Shirt",
                    price = 22.3,
                    description = "Slim-fitting style",
                    category = "men's clothing",
                    image = "https://fakestoreapi.com/img/mens-t-shirt.jpg",
                    rating = Rating(rate = 4.1, count = 259)
                )
            )


            coEvery { externalApiService.getProductsByCategory("men's clothing") } returns Response.success(categoryProducts)


            val result = repository.getExternalProductsByCategory("men's clothing")


            result.size shouldBe 1
            result.first().name shouldBe "Mens Casual T-Shirt"
        }
    }

    "Cuando obtengo categorías externas, debe retornar la lista de nombres" {
        runTest {

            val categories = listOf("electronics", "jewelery", "men's clothing", "women's clothing")


            coEvery { externalApiService.getCategories() } returns Response.success(categories)


            val result = repository.getExternalCategories()


            result shouldBe categories
        }
    }
})
