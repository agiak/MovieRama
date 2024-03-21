package com.example.movierama.features.home.presentation.viewholders.toprated

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.R
import com.example.movierama.databinding.ItemHomeTopRatedBinding
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.presentation.viewholders.HomeMovieDiffCallback
import com.example.movierama.core.presentation.utils.load
import com.example.movierama.core.presentation.utils.loadCircle

class TopRatedAdapter(
    private val onClick: (movieId: Long) -> Unit = {},
) : ListAdapter<Movie, TopRatedAdapter.MovieViewHolder>(HomeMovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeTopRatedBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemHomeTopRatedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.poster.load(url = movie.poster, placeholder = R.drawable.ic_movie_placeholder)
            binding.logo.loadCircle(url = movie.poster, placeholder = R.drawable.ic_movie_placeholder)

            binding.title.text = movie.title
            binding.rating.text = movie.rating.toString()

            binding.root.setOnClickListener {
                onClick(movie.id)
            }
        }
    }
}
