package com.example.skai.data.repository

import com.example.skai.data.api.ProductApiService
import com.example.skai.data.database.dao.ProductDao
import com.example.skai.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productApiService: ProductApiService
) {

    suspend fun syncProductsFromApi() {
        try {
            val response = productApiService.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                products.forEach { product ->
                    productDao.insertProduct(product)
                }
            }
        } catch (e: Exception) {

        }
    }

    fun getAllActiveProducts(): Flow<List<Product>> {
        return flow {

            syncProductsFromApi()

            emitAll(productDao.getAllActiveProducts())
        }
    }

    suspend fun getProductById(productId: String): Product? {

        return try {
            val response = productApiService.getProductById(productId)
            if (response.isSuccessful) {
                val product = response.body()
                product?.let { productDao.insertProduct(it) }
                product
            } else {
                productDao.getProductById(productId)
            }
        } catch (e: Exception) {
            productDao.getProductById(productId)
        }
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return flow {

            try {
                val response = productApiService.getProductsByCategory(category)
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    products.forEach { product ->
                        productDao.insertProduct(product)
                    }
                }
            } catch (e: Exception) {

            }
            emitAll(productDao.getProductsByCategory(category))
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    suspend fun insertProduct(product: Product) {

        try {
            val response = productApiService.createProduct(product)
            if (response.isSuccessful) {
                val createdProduct = response.body()
                createdProduct?.let { productDao.insertProduct(it) }
            } else {

                productDao.insertProduct(product)
            }
        } catch (e: Exception) {

            productDao.insertProduct(product)
        }
    }

    suspend fun updateProduct(product: Product) {

        try {
            val response = productApiService.updateProduct(product.id, product)
            if (response.isSuccessful) {
                val updatedProduct = response.body()
                updatedProduct?.let { productDao.updateProduct(it) }
            } else {

                productDao.updateProduct(product)
            }
        } catch (e: Exception) {

            productDao.updateProduct(product)
        }
    }

    suspend fun deleteProduct(product: Product) {

        try {
            val response = productApiService.deleteProduct(product.id)
            if (response.isSuccessful) {
                productDao.deleteProduct(product)
            } else {

                productDao.deleteProduct(product)
            }
        } catch (e: Exception) {

            productDao.deleteProduct(product)
        }
    }

    suspend fun updateProductStock(productId: String, newStock: Int) {
        val product = productDao.getProductById(productId)
        product?.let {
            val updatedProduct = it.copy(stock = newStock)
            updateProduct(updatedProduct)
        }
    }
}
