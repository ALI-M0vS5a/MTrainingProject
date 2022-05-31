package com.example.fetchgate.network

import retrofit2.http.GET

interface UserApi {
    @GET("user")
    suspend fun getUser(): LoginResponse
}