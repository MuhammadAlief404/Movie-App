package com.quantumhiggs.movieapp.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.quantumhiggs.movieapp.data.Movie
import com.quantumhiggs.movieapp.data.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private var currentMovie: Flow<PagingData<Movie>>? = null

    fun getAllMovies(): Flow<PagingData<Movie>> {
        val lastResult = currentMovie
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Movie>> = repository.getAllMovies()
            .cachedIn(viewModelScope)
        currentMovie = newResult
        return newResult
    }

    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return repository.getMoviesByGenre(genreId)
            .cachedIn(viewModelScope)
    }
}