package com.example.fetchgate.repository

import com.example.fetchgate.language.data.UserPreferences
import com.example.fetchgate.network.ApiService

class AuthRepository(
    private val api: ApiService,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }

}