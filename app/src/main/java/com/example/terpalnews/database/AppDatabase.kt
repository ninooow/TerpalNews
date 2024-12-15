package com.example.terpalnews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import com.example.terpalnews.database.entities.UserEntity
import androidx.room.RoomDatabase
import com.example.terpalnews.database.dao.NewsDao
import com.example.terpalnews.database.dao.UserDao
import com.example.terpalnews.database.entities.NewsEntity

@Database(entities = [UserEntity::class, NewsEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(), NewsDao {
    abstract fun noteDao(): UserDao?
    abstract fun newsDao(): NewsDao?
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "app_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
