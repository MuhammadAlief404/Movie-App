package com.quantumhiggs.movieapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.quantumhiggs.movieapp.data.DetailMovieResponse
import com.quantumhiggs.movieapp.data.MovieRepository
import com.quantumhiggs.movieapp.data.remote.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private var _movie = MutableLiveData<DetailMovieResponse>()
    val movie get() = _movie

    private var _video = MutableLiveData<String>()
    val video get() = _video

    private var currentReview: Flow<PagingData<Review>>? = null

    fun getDetailMovie(id: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getDetailMovies(id)
            }
            _movie.postValue(result)
        }
    }

    fun getMovieVideo(id: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getMovieTrailer(id)
            }
            val video = result.results
            if (video != null && video.size > 1) {
                loop@ for (link in video) {
                    if (link?.site == "YouTube") {
                        val url = link.key.toString()
                        _video.postValue(url)
                        break@loop
                    }
                }
            }
        }
    }

    fun getReview(id: Int): Flow<PagingData<Review>> {
        val lastResult = currentReview
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Review>> = repository.getMovieReviews(id)
            .cachedIn(viewModelScope)
        currentReview = newResult
        return newResult
    }
}
