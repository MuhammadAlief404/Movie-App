package com.quantumhiggs.movieapp.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quantumhiggs.movieapp.BuildConfig
import com.quantumhiggs.movieapp.R
import com.quantumhiggs.movieapp.data.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_IMG + movie.poster_path)
                .into(itemView.movie_img)
            itemView.movie_name.text = movie.title

            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(MovieFragmentDirections.toDetailMovie(movie.id))
            }
        }
    }
}