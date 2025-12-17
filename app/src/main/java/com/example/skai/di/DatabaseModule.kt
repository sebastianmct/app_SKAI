package com.example.skai.di

import com.example.skai.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: com.example.skai.data.api.UserApiService
    ): UserRepository {
        return UserRepository(userApiService)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productApiService: com.example.skai.data.api.ProductApiService
    ): ProductRepository {
        return ProductRepository(productApiService)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartApiService: com.example.skai.data.api.CartApiService
    ): CartRepository {
        return CartRepository(cartApiService)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        orderApiService: com.example.skai.data.api.OrderApiService
    ): OrderRepository {
        return OrderRepository(orderApiService)
    }
    
    @Provides
    @Singleton
    fun provideExternalProductRepository(
        externalProductApiService: com.example.skai.data.api.ExternalProductApiService
    ): com.example.skai.data.repository.ExternalProductRepository {
        return com.example.skai.data.repository.ExternalProductRepository(externalProductApiService)
    }
}
