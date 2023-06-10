package com.example.movierama.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.data.Movie
import com.example.movierama.databinding.ItemMovieBinding

class MovieAdapter (
    private val onClick: (movie: Movie) -> Unit,
    private val onFavouriteClick: (movieId: Long) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(movie){
                Glide.with(context)
                    .load(poster)
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_movie_placeholder))
                    .into(binding.logo)
                binding.title.text = title
                binding.rating.rating = rating
                binding.releaseDate.text = releaseDate

                binding.root.setOnClickListener {
                    onClick(movie)
                }
                binding.favouriteBtn.apply {
                    setOnClickListener {
                        with(binding.favouriteBtn){
                            isSelected = isSelected.not()
                        }
                        onFavouriteClick(this@with.id)
                    }
                    isSelected = isFavourite
                }
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
