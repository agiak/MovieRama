package com.example.movierama.ui.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.data.network.reviews.Review
import com.example.movierama.data.network.similar.SimilarMovie
import com.example.movierama.databinding.ItemSimilarMovieBinding

class SimilarMovieAdapter: ListAdapter<SimilarMovie, SimilarMovieAdapter.SimilarMovieViewHolder>(SimilarMovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSimilarMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return SimilarMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val similarMovie = getItem(position)
        holder.bind(similarMovie)
    }

    inner class SimilarMovieViewHolder(private val binding: ItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(similarMovie: SimilarMovie) {
            with(similarMovie){
                Glide.with(context)
                    .load(similarMovie.poster)
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_movie_placeholder))
                    .into(binding.poster)
            }
        }
    }

    private class SimilarMovieDiffCallback : DiffUtil.ItemCallback<SimilarMovie>() {
        override fun areItemsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean {
            return oldItem == newItem
        }
    }
}
