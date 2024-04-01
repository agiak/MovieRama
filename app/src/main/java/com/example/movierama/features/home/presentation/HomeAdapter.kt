package com.example.movierama.features.home.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.databinding.ItemSimilarMovieBinding
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.presentation.viewholders.HomeMovieDiffCallback

class HomeAdapter(
    private val onClick: (movieId: Long) -> Unit = {},
) : ListAdapter<Movie, HomeAdapter.MovieViewHolder>(HomeMovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSimilarMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            Glide.with(context).load(movie.poster).placeholder(
                    ContextCompat.getDrawable(
                        context, R.drawable.ic_movie_placeholder
                    )
                ).into(binding.poster)

            binding.root.setOnClickListener {
                onClick(movie.id)
            }
        }
    }
}