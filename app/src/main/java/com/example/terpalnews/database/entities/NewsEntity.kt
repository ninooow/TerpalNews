package com.example.terpalnews.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey @SerializedName("_id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo("uploadDateTime") val uploadDateTime: Long,
    @ColumnInfo("image") val image: String,
    @ColumnInfo("content") val content: String
)

