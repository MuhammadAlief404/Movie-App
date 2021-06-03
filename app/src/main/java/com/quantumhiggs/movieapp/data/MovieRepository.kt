package com.quantumhiggs.movieapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.data.remote.Review
import com.quantumhiggs.movieapp.network.ApiServices
import com.quantumhiggs.movieapp.util.Cons.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val service: ApiServices) {

    fun getAllMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(service) }
        ).flow
    }

    suspend fun getDetailMovies(id: Int) = service.getMoviesDetail(id, BuildConfig.API_KEY)

    suspend fun getMovieTrailer(id: Int) = service.getMovieTrailer(id, BuildConfig.API_KEY)

    fun getMovieReviews(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ReviewPagingSource(service, movieId) }
        ).flow
    }

    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieGenrePagingSource(service, genreId) }
        ).flow
    }

    suspend fun getGenres() = service.getGenres(BuildConfig.API_KEY)
}