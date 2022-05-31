package com.example.fetchgate.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchgate.network.LoginResponse
import com.example.fetchgate.repository.UserRepository
import com.example.fetchgate.utils.Resource
import com.example.fetchgate.utils.ResourceAuth
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _user: MutableLiveData<ResourceAuth<LoginResponse>> = MutableLiveData()
    val user: LiveData<ResourceAuth<LoginResponse>>
        get() = _user


    fun getUser() = viewModelScope.launch {
            _user.value = ResourceAuth.Loading
            _user.value = repository.getUser()
    }
}