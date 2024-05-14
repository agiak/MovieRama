package com.example.movierama.features.movies.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.databinding.ItemMovieBinding


class MovieTypeAdapter(
    private val onClick: (movie: Movie) -> Unit,
    private val onFavouriteClick: (movie: Movie) -> Unit
): PagingDataAdapter<Movie, MovieTypeAdapter.MovieViewHolder>(MovieListDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item: Movie? = getItem(position)
        item?.let { holder.bind(it) }
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val newItem = payloads[0] as Movie
            holder.bind(newItem)
        }
    }

    private class MovieListDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(movie){
                Glide.with(binding.root.context)
                    .load(poster)
                    .placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_movie_placeholder))
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
                        onFavouriteClick(movie)
                    }
                    isSelected = isFavourite
                }
            }
        }
    }

}