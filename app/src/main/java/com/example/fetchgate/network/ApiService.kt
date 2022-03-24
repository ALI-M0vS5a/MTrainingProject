package com.example.fetchgate.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("repos")
    suspend fun getDataFromApi(
        @Query("page") query: Int
    ): List<Result>

}