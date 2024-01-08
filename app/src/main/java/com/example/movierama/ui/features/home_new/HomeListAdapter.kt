package com.example.movierama.ui.features.home_new

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.model.Movie
import com.example.movierama.model.MoviesType
import com.example.movierama.ui.utils.addOnLoadMoreListener

class HomeListAdapter(
    private val onLabelClicked: (label: String) -> Unit = {},
    private val onItemClick: (movieId: Long) -> Unit = {},
    private val fetchingMovies: (movieType: MoviesType) -> Unit = {}
) : ListAdapter<HomeList, HomeListAdapter.MovieViewHolder>(HomeListDiffCallback()) {

    private lateinit var context: Context

    private val popularAdapter = HomeMovieAdapter(onClick = {
        onItemClick(it)
    })

    private val topRatedAdapter = HomeMovieAdapter(onClick = {
        onItemClick(it)
    })

    private val nowPlayingAdapter = HomeMovieAdapter(onClick = {
        onItemClick(it)
    })

    private val upcomingAdapter = HomeMovieAdapter(onClick = {
        onItemClick(it)
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeListBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val similarMovie = getItem(position)
        holder.bind(similarMovie)
    }

    inner class MovieViewHolder(private val binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(selectedList: HomeList) {
            with(selectedList){
                binding.label.apply {
                    text = label
                    setOnClickListener { onLabelClicked(label) }
                }

                binding.list.apply {
                    val adapter = getAdapter(moviesType)
                    this.adapter = adapter
                    adapter.submitList(movies)
                    addOnLoadMoreListener { fetchingMovies(moviesType) }
                }
            }
        }
    }

    private fun submitMoviesByType(movieType: MoviesType, newMovies: List<Movie>) {
        when(movieType) {
            MoviesType.POPULAR -> popularAdapter.submitList(newMovies)
            MoviesType.NOW_PLAYING -> nowPlayingAdapter.submitList(newMovies)
            MoviesType.TOP_RATED -> topRatedAdapter.submitList(newMovies)
            MoviesType.UPCOMING -> upcomingAdapter.submitList(newMovies)
        }
    }

    private fun getAdapter(movieType: MoviesType): HomeMovieAdapter =
        when(movieType){
            MoviesType.POPULAR -> popularAdapter
            MoviesType.NOW_PLAYING -> nowPlayingAdapter
            MoviesType.TOP_RATED -> topRatedAdapter
            MoviesType.UPCOMING -> upcomingAdapter
        }

    private class HomeListDiffCallback : DiffUtil.ItemCallback<HomeList>() {
        override fun areItemsTheSame(oldItem: HomeList, newItem: HomeList): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: HomeList, newItem: HomeList): Boolean {
            return oldItem == newItem
        }
    }
}
