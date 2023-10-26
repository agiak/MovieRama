package com.example.movierama.ui.features.search_movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.databinding.ItemSearchMovieBinding
import com.example.movierama.model.search_movie.SearchedMovie

class SearchedMoviesAdapter(
    private val onClick: (movie: SearchedMovie) -> Unit,
) : ListAdapter<SearchedMovie, SearchedMoviesAdapter.MovieViewHolder>(MovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: SearchedMovie) {
            with(movie) {
                Glide.with(context)
                    .load(logo)
                    .circleCrop()
                    .placeholder(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_movie_placeholder
                        )
                    )
                    .into(binding.logo)
                binding.title.text = title

                binding.root.setOnClickListener {
                    onClick(movie)
                }
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<SearchedMovie>() {
        override fun areItemsTheSame(oldItem: SearchedMovie, newItem: SearchedMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchedMovie, newItem: SearchedMovie): Boolean {
            return oldItem == newItem
        }
    }
}
