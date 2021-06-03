package com.quantumhiggs.movieapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.network.ApiServices
import com.quantumhiggs.movieapp.util.Cons
import retrofit2.HttpException
import java.io.IOException

class MovieGenrePagingSource(
    private val service: ApiServices,
    private val genreId: Int
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: Cons.STARTING_PAGE
        val apiQuery = BuildConfig.API_KEY
        return try {
            val response = service.getMovieByGenre(apiQuery, genreId, position)
            val repos = response.results
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / Cons.NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == Cons.STARTING_PAGE) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}