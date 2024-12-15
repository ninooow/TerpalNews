package com.example.terpalnews.network

import androidx.compose.ui.graphics.vector.Path
import com.example.terpalnews.database.entities.NewsEntity
import com.example.terpalnews.database.entities.UserEntity
import com.example.terpalnews.model.News
import com.example.terpalnews.model.User
import com.example.terpalnews.model.UserSend
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("user")
    fun getAllUsers(): Call<List<User>>
    @GET("news")
    fun getAllNews(): Call<List<News>>
    @POST("user")
    fun registerUser(@Body user: User): Call<User>
    @POST("news")
    fun uploadNews(@Body news: News): Call<News>
    @POST("data/{id}")
    fun updateNews(@Path("id") id: String, @Body news: News): Call<News>
    @DELETE("data/{id}")
    fun deleteNews(@Path("id") id: String): Call<Void>
}