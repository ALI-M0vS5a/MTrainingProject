package com.example.fetchgate.overview

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fetchgate.PagingSource
import com.example.fetchgate.network.ApiInstance
import com.example.fetchgate.network.ApiService
import com.example.fetchgate.network.Result
import kotlinx.coroutines.flow.Flow

class OverviewViewModel : ViewModel() {
    private var apiService: ApiService = ApiInstance.getApiInstance().create(
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
}