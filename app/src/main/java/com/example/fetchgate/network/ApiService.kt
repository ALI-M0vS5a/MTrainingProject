package com.example.fetchgate.network

import retrofit2.http.*

interface ApiService {
    @GET("repos")
    suspend fun getDataFromApi(
        @Query("page") query: Int
    ): List<Result>


    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse
}