package com.quantumhiggs.movieapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.data.remote.Review
import com.quantumhiggs.movieapp.network.ApiServices
import com.quantumhiggs.movieapp.util.Cons
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val service: ApiServices,
    private val movieId: Int
) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val position = params.key ?: Cons.STARTING_PAGE
        val apiQuery = BuildConfig.API_KEY
        return try {
            val response = service.getMovieReviews(movieId, apiQuery, position)
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