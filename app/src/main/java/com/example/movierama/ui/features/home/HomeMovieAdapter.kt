package com.example.movierama.ui.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.databinding.ItemSimilarMovieBinding
import com.example.movierama.model.Movie

class HomeMovieAdapter(
    private val onClick: (movieId: Long) -> Unit = {},
) : ListAdapter<Movie, HomeMovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSimilarMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val similarMovie = getItem(position)
        holder.bind(similarMovie)
    }

    inner class MovieViewHolder(private val binding: ItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(similarMovie: Movie) {
            Glide.with(context).load(similarMovie.poster).placeholder(
                    ContextCompat.getDrawable(
                        context, R.drawable.ic_movie_placeholder
                    )
                ).into(binding.poster)

            binding.root.setOnClickListener {
                onClick(similarMovie.id)
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
