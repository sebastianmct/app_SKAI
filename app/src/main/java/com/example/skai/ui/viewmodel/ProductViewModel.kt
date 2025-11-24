package com.example.skai.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skai.DataManager
import com.example.skai.data.model.Product
import com.example.skai.data.repository.ExternalProductRepository
import com.example.skai.utils.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val externalProductRepository: ExternalProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _externalProducts = MutableStateFlow<List<Product>>(emptyList())
    val externalProducts: StateFlow<List<Product>> = _externalProducts.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProducts()
        loadExternalProducts()
        observeProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _products.value = DataManager.getAllProducts()
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadExternalProducts() {
        viewModelScope.launch {
            try {
                val externalProducts = externalProductRepository.getAllExternalProducts()
                _externalProducts.value = externalProducts
                

                if (externalProducts.isNotEmpty()) {
                    val firstProduct = externalProducts.first()

                }
            } catch (e: Exception) {

            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            combine(
                _products,
                _externalProducts,
                _selectedCategory,
                _searchQuery
            ) { products, externalProducts, category, query ->

                val allProducts = products + externalProducts
                allProducts.filter { product ->
                    val matchesCategory = category == null || product.category == category
                    val matchesQuery = query.isEmpty() || 
                        product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
                    matchesCategory && matchesQuery
                }
            }.collect { filtered ->
                _filteredProducts.value = filtered
            }
        }
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getCategories(): List<String> {
        return DataManager.getCategories()
    }

    suspend fun getProductById(productId: String): Product? {
        return DataManager.getProductById(productId)
    }

    suspend fun addProduct(product: Product, context: Context? = null) {
        DataManager.addProduct(product)
        loadProducts()
        

        context?.let {
            NotificationService.notifyNewProduct(it, product.name)
        }
    }

    suspend fun updateProduct(product: Product) {
        DataManager.updateProduct(product)
        loadProducts()
    }

    suspend fun deleteProduct(product: Product) {
        DataManager.deleteProduct(product)
        loadProducts()
    }
}
