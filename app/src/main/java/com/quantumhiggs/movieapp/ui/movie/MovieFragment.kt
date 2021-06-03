package com.quantumhiggs.movieapp.ui.movie

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.quantumhiggs.movieapp.R
import com.quantumhiggs.movieapp.databinding.FragmentMovieBinding
import com.quantumhiggs.movieapp.di.Injection
import com.quantumhiggs.movieapp.util.Cons.GENRE_KEY
import com.quantumhiggs.movieapp.util.LoadStateAdapter
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private val binding: FragmentMovieBinding by viewBinding()
    private lateinit var viewModel: MovieViewModel
    private val adapter = MoviesAdapter()
    private var movieJob: Job? = null
    private var genreMovieJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(GENRE_KEY)
            ?.observe(viewLifecycleOwner) { genreId ->
                getMovieByGenres(genreId)
            }

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(MovieViewModel::class.java)

        initAdapter()
        getMovies()
    }

    private fun getMovies() {
        movieJob?.cancel()
        movieJob = lifecycleScope.launch {
            viewModel.getAllMovies().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun getMovieByGenres(genreId: Int) {
        genreMovieJob?.cancel()
        genreMovieJob = lifecycleScope.launch {
            viewModel.getMoviesByGenre(genreId).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        binding.listMovie.layoutManager = GridLayoutManager(context, 2)
        binding.listMovie.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { adapter.retry() },
            footer = LoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            binding.listMovie.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(context, "\\uD83D\\uDE28 Wooops ${it.error}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.listMovie.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.listMovie.visibility = View.VISIBLE
        }
    }
}