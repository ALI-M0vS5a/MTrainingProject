package com.example.fetchgate.repository

import com.example.fetchgate.utils.ResourceAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ResourceAuth<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResourceAuth.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        ResourceAuth.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        ResourceAuth.Failure(true, null, null)

                    }
                }
            }
        }
    }
}