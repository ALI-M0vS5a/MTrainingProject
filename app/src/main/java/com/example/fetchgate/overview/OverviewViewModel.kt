package com.example.fetchgate.overview

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
}