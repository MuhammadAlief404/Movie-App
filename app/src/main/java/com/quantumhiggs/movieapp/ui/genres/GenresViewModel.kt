package com.quantumhiggs.movieapp.ui.genres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantumhiggs.movieapp.data.MovieRepository
import com.quantumhiggs.movieapp.data.remote.GenresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenresViewModel(private val repository: MovieRepository) : ViewModel() {

    private var _genre = MutableLiveData<GenresResponse>()
    val genre get() = _genre

    fun getGenres() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getGenres()
            }
            _genre.postValue(result)
        }
    }

}