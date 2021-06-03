package com.quantumhiggs.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quantumhiggs.movieapp.data.MovieRepository
import com.quantumhiggs.movieapp.ui.detail.DetailMovieViewModel
import com.quantumhiggs.movieapp.ui.genres.GenresViewModel
import com.quantumhiggs.movieapp.ui.movie.MovieViewModel

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(GenresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GenresViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailMovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}