package com.example.fetchgate.overview

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import androidx.paging.*
import com.example.fetchgate.PagingSource
import com.example.fetchgate.network.ApiInstance
import com.example.fetchgate.network.ApiService
import com.example.fetchgate.network.LoginResponse
import com.example.fetchgate.network.Result
import com.example.fetchgate.repository.UserRepository
import com.example.fetchgate.utils.ResourceAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OverviewViewModel: ViewModel() {
    private var apiService = ApiInstance.getApiInstance().create(
        ApiService::class.java
    )

    fun getListData(): Flow<PagingData<Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PagingSource(apiService)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            return true
        }
    }
//
//    private val _user: MutableLiveData<ResourceAuth<LoginResponse>> = MutableLiveData()
//    val user: LiveData<ResourceAuth<LoginResponse>>
//        get() = _user
//
//
//    fun getUser() = viewModelScope.launch {
//        _user.value = repository.getUser()
//    }
}