package com.example.movierama.features.home.presentation.viewholders

import androidx.recyclerview.widget.DiffUtil
import com.example.movierama.core.data.movies.Movie

class HomeMovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}