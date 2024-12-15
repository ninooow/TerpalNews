package com.example.terpalnews.model

import com.google.gson.annotations.SerializedName

data class UserSend(
    @SerializedName("name")
    val name:String,
    @SerializedName("username")
    val username:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String,
    @SerializedName("role")
    val role:String = "public"
)
