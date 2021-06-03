package com.quantumhiggs.movieapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.quantumhiggs.movieapp.R
import com.quantumhiggs.movieapp.data.remote.Review
import kotlinx.android.synthetic.main.item_review.view.*

class MovieReviewAdapter :
    PagingDataAdapter<Review, MovieReviewAdapter.ReviewViewHolder>(REVIEW_COMPARATOR) {

    companion object {
        private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review: Review) {
            itemView.review_name.text = review.author
            itemView.review_content.text = review.content
        }
    }
}