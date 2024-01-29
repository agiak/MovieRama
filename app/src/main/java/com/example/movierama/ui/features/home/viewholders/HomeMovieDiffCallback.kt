package com.example.movierama.ui.features.home.viewholders

import androidx.recyclerview.widget.DiffUtil
import com.example.movierama.model.Movie

class HomeMovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}