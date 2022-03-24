package com.example.fetchgate.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Result(
    @SerializedName("full_name")
    val full_name: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner")
    val owner: Owner
) : Serializable

data class Owner(
    @SerializedName("avatar_url")
    val avatar_url: String
) : Serializable