package com.quantumhiggs.movieapp.di

import androidx.lifecycle.ViewModelProvider
import com.quantumhiggs.movieapp.data.MovieRepository
import com.quantumhiggs.movieapp.network.NetworkConfig
import com.quantumhiggs.movieapp.viewmodel.ViewModelFactory

object Injection {

    private fun provideMovieRepository(): MovieRepository {
        return MovieRepository(NetworkConfig.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieRepository())
    }
}