package com.example.fetchgate.utils

import okhttp3.ResponseBody

sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?, data: T? = null): Resource<T>(data,message)
    class Loading<T>(data: T? = null): Resource<T>(data)
}


sealed class ResourceAuth<out T> {
    data class Success<out T>(val value: T) : ResourceAuth<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ): ResourceAuth<Nothing>()
    object Loading : ResourceAuth<Nothing>()
}
