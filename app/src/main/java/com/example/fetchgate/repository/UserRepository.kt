package com.example.fetchgate.repository

import com.example.fetchgate.network.UserApi

class UserRepository(
    private val api: UserApi
): BaseRepository(){
    suspend fun getUser() = safeApiCall {
        api.getUser()
    }
}