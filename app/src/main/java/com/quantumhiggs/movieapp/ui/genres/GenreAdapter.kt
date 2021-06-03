package com.quantumhiggs.movieapp.ui.genres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quantumhiggs.movieapp.R
import com.quantumhiggs.movieapp.data.remote.GenresItem
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(private val listener: (GenresItem) -> Unit) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var listGenre = ArrayList<GenresItem>()

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(genre: GenresItem) {
            itemView.genre_name.text = genre.name

            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = listGenre.get(position)
        if (genre != null) {
            holder.bind(genre)
        }
        holder.itemView.setOnClickListener {
            listener(listGenre[position])
        }
    }

    override fun getItemCount() = listGenre.size

    fun updateData(listGenre: List<GenresItem>) {
        this.listGenre.clear()
        this.listGenre.addAll(listGenre)
        notifyDataSetChanged()
    }
}