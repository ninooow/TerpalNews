package com.example.terpalnews.model

import com.google.gson.annotations.SerializedName


data class News (
    @SerializedName("_id")
    val id : String?=null,
    @SerializedName("title")
    val title: String,
    @SerializedName("uploadDateTime")
    val uploadDateTime: Long,
    @SerializedName("image")
    val image: String,
    @SerializedName("content")
    val content: String="public"
)