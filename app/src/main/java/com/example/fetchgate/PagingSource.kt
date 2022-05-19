package com.example.fetchgate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fetchgate.network.ApiService
import com.example.fetchgate.network.Result
import retrofit2.HttpException
import java.io.IOException

class PagingSource
constructor(
    private val apiService: ApiService,
) : PagingSource<Int, Result>() {

    companion object {
        private const val DEFAULT_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getDataFromApi(page)

            LoadResult.Page(
                response,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
