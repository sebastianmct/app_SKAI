package com.example.skai.di

import android.content.Context
import com.example.skai.data.database.SkaiDatabase
import com.example.skai.data.database.DatabaseInitializer
import com.example.skai.data.database.dao.*
import com.example.skai.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SkaiDatabase {
        return SkaiDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserDao(database: SkaiDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideProductDao(database: SkaiDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideCartDao(database: SkaiDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideOrderDao(database: SkaiDatabase): OrderDao {
        return database.orderDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao,
        productApiService: com.example.skai.data.api.ProductApiService
    ): ProductRepository {
        return ProductRepository(productDao, productApiService)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderDao: OrderDao): OrderRepository {
        return OrderRepository(orderDao)
    }
    
    @Provides
    @Singleton
    fun provideExternalProductRepository(
        externalProductApiService: com.example.skai.data.api.ExternalProductApiService
    ): com.example.skai.data.repository.ExternalProductRepository {
        return com.example.skai.data.repository.ExternalProductRepository(externalProductApiService)
    }

    @Provides
    @Singleton
    fun provideDatabaseInitializer(
        userDao: UserDao,
        productDao: ProductDao
    ): DatabaseInitializer {
        return DatabaseInitializer(userDao, productDao)
    }
}
