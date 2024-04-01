package com.example.movierama.features.home.presentation.viewholders.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.R
import com.example.movierama.databinding.ItemHomeUpcomingBinding
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.presentation.viewholders.HomeMovieDiffCallback
import com.example.movierama.core.presentation.utils.load

class UpcomingAdapter(
    private val onClick: (movieId: Long) -> Unit = {},
) : ListAdapter<Movie, UpcomingAdapter.MovieViewHolder>(HomeMovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeUpcomingBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemHomeUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.poster.load(url = movie.poster, placeholder = R.drawable.ic_movie_placeholder)

            binding.title.text = movie.title
            binding.releaseDate.text = "Coming at: ${movie.releaseDate}"

            binding.posterCard.setOnClickListener {
                onClick(movie.id)
            }
        }
    }
}