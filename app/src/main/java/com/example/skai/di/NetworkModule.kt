package com.example.skai.di

import com.example.skai.data.api.ExternalProductApiService
import com.example.skai.data.api.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    

    private const val BASE_URL = "https://api.example.com/api/" // TODO: Actualizar con la URL real de tu API
    private const val EXTERNAL_API_BASE_URL = "https://fakestoreapi.com/"
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    @Named("internal")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("external")
    fun provideExternalRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EXTERNAL_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideProductApiService(@Named("internal") retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideExternalProductApiService(@Named("external") retrofit: Retrofit): ExternalProductApiService {
        return retrofit.create(ExternalProductApiService::class.java)
    }
}

