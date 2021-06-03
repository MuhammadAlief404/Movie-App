package com.quantumhiggs.movieapp.network

import com.quantumhiggs.movieapp.data.DetailMovieResponse
import com.quantumhiggs.movieapp.data.MoviesResponse
import com.quantumhiggs.movieapp.data.remote.GenresResponse
import com.quantumhiggs.movieapp.data.remote.MovieReviewResponse
import com.quantumhiggs.movieapp.data.remote.MovieVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("movie/popular")
    suspend fun getAllMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/{id}")
    suspend fun getMoviesDetail(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): DetailMovieResponse

    @GET("movie/{id}/videos")
    suspend fun getMovieTrailer(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): MovieVideoResponse

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): MovieReviewResponse

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") key: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") key: String,
    ): GenresResponse
}