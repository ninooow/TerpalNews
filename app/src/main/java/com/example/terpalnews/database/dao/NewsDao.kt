package com.example.terpalnews.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.terpalnews.database.entities.NewsEntity
import com.example.terpalnews.model.News

@Dao
interface NewsDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertNews(news: NewsEntity)

//    @Update
//    fun updateNews(news: NewsEntity)
//
//    @Delete
//    fun deleteNews(news: NewsEntity)

//    @Query("SELECT * FROM news WHERE id = :newsId")
//    suspend fun getNewsById(newsId: String)

//    @Query("SELECT * FROM news ORDER BY id ASC")
//    suspend fun getAllNews(): List<NewsEntity>
}