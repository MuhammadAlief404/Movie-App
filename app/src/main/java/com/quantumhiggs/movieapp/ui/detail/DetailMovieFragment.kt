package com.quantumhiggs.movieapp.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.R
import com.quantumhiggs.movieapp.databinding.FragmentDetailMovieBinding
import com.quantumhiggs.movieapp.di.Injection
import com.quantumhiggs.movieapp.util.LoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private val binding: FragmentDetailMovieBinding by viewBinding()
    private lateinit var viewModel: DetailMovieViewModel
    private val adapter = MovieReviewAdapter()

    val args by navArgs<DetailMovieFragmentArgs>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(DetailMovieViewModel::class.java)

        initAdapter()
        getReview()

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.listReview.addItemDecoration(decoration)

        viewModel.getDetailMovie(args.movieId)
        viewModel.getMovieVideo(args.movieId)

        viewModel.movie.observe(viewLifecycleOwner, Observer { movie ->
            val genres = StringBuilder()
            binding.tvTitle.text = movie.title
            binding.tvOverview.text = movie.overview
            movie.genres?.forEach {
                genres
                    .append(it?.name)
                    .append(", ")
            }
            binding.tvGenre.text = genres.toString().dropLast(2)
            Glide.with(this)
                .load(BuildConfig.BASE_IMG + movie.posterPath)
                .into(binding.imgBanner)
        })

        viewModel.video.observe(viewLifecycleOwner, Observer {
            binding.videoPlayer.getPlayerUiController().showFullscreenButton(true)
            binding.videoPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = it
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        })
    }

    private var reviewJob: Job? = null
    private fun getReview() {
        reviewJob?.cancel()
        reviewJob = lifecycleScope.launch {
            viewModel.getReview(args.movieId).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        binding.listReview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.listReview.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { adapter.retry() },
            footer = LoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            binding.listReview.isVisible = loadState.source.refresh is LoadState.NotLoading
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
            binding.listReview.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.listReview.visibility = View.VISIBLE
        }
    }
}