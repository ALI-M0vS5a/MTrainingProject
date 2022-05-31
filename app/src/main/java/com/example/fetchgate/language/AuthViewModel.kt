package com.example.fetchgate.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchgate.network.LoginResponse
import com.example.fetchgate.repository.AuthRepository
import com.example.fetchgate.utils.ResourceAuth
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse: MutableLiveData<ResourceAuth<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<ResourceAuth<LoginResponse>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = repository.login(email, password)
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }

}