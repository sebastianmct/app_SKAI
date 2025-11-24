package com.example.skai.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.skai.data.database.dao.*
import com.example.skai.data.model.*

@Database(
    entities = [User::class, Product::class, CartItem::class, Order::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SkaiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: SkaiDatabase? = null

        fun getDatabase(context: Context): SkaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkaiDatabase::class.java,
                    "skai_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
