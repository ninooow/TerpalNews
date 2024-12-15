package com.example.terpalnews.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.terpalnews.database.entities.UserEntity
import com.example.terpalnews.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser (user: UserEntity)

    @Update
    fun updateUser (user: UserEntity)

    @Delete
    fun deleteUser (user: UserEntity)

//    @Query("SELECT * FROM user WHERE username = :username")
//    suspend fun getUserByUsername(username: String)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>
}