package com.example.skai.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.skai.data.SampleData
import com.example.skai.data.database.dao.UserDao
import com.example.skai.data.database.dao.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val userDao: UserDao,
    private val productDao: ProductDao
) {
    
    fun initializeDatabase() {
        CoroutineScope(Dispatchers.IO).launch {

            val userCount = userDao.getUserByEmail("admin@skai.com")
            

            if (userCount == null) {
                SampleData.sampleUsers.forEach { user ->
                    userDao.insertUser(user)
                }
            }
            

            val products = productDao.getAllActiveProducts()
            products.collect { productList ->
                if (productList.isEmpty()) {
                    SampleData.sampleProducts.forEach { product ->
                        productDao.insertProduct(product)
                    }
                }
            }
        }
    }
}
