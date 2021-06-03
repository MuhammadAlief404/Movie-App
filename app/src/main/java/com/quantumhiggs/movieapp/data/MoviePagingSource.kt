package com.quantumhiggs.movieapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.network.ApiServices
import com.quantumhiggs.movieapp.util.Cons.NETWORK_PAGE_SIZE
import com.quantumhiggs.movieapp.util.Cons.STARTING_PAGE
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val service: ApiServices
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE
        val apiQuery = BuildConfig.API_KEY
        return try {
            val response = service.getAllMovies(apiQuery, position)
            val repos = response.results
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_PAGE) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}