package com.example.movierama.features.home.presentation.viewholders.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.R
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.presentation.utils.load
import com.example.movierama.databinding.ItemHomeUpcomingBinding
import com.example.movierama.features.home.presentation.viewholders.HomeMovieDiffCallback

class UpcomingPagingAdapter(
    private val onClick: (movieId: Long) -> Unit,
): PagingDataAdapter<Movie, UpcomingPagingAdapter.MovieViewHolder>(HomeMovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeUpcomingBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item: Movie? = getItem(position)
        item?.let { holder.bind(it) }
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