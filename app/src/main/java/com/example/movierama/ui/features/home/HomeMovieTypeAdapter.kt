package com.example.movierama.ui.features.home

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

class HomeMovieTypeAdapter(
    private val onLabelClicked: (label: MoviesType) -> Unit = {},
    private val onItemClick: (movieId: Long) -> Unit = {},
    private val onFetchingMovies: (movieType: MoviesType) -> Unit = {}
) : ListAdapter<HomeMovieTypeList, HomeMovieTypeAdapter.MovieViewHolder>(HomeMovieTypeDiffCallback()) {

    private lateinit var context: Context

    private val popularAdapter = HomeAdapter(onClick = {
        onItemClick(it)
    })

    private val topRatedAdapter = HomeAdapter(onClick = {
        onItemClick(it)
    })

    private val nowPlayingAdapter = HomeAdapter(onClick = {
        onItemClick(it)
    })

    private val upcomingAdapter = HomeAdapter(onClick = {
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

        fun bind(selectedList: HomeMovieTypeList) {
            with(selectedList){
                binding.label.apply {
                    text = label
                    setOnClickListener { onLabelClicked(moviesType) }
                }

                binding.list.apply {
                    val adapter = getAdapter(moviesType)
                    this.adapter = adapter
                    adapter.submitList(movies)
                    addOnLoadMoreListener { onFetchingMovies(moviesType) }
                }
            }
        }
    }

    fun submitMoviesByType(movieType: MoviesType, newMovies: List<Movie>) {
        val currentList = getAdapter(movieType).currentList.toMutableList()
        currentList.addAll(newMovies)
        getAdapter(movieType).submitList(currentList)
    }

    private fun getAdapter(movieType: MoviesType): HomeAdapter =
        when(movieType){
            MoviesType.POPULAR -> popularAdapter
            MoviesType.NOW_PLAYING -> nowPlayingAdapter
            MoviesType.TOP_RATED -> topRatedAdapter
            MoviesType.UPCOMING -> upcomingAdapter
        }

    private class HomeMovieTypeDiffCallback : DiffUtil.ItemCallback<HomeMovieTypeList>() {
        override fun areItemsTheSame(oldItem: HomeMovieTypeList, newItem: HomeMovieTypeList): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: HomeMovieTypeList, newItem: HomeMovieTypeList): Boolean {
            return oldItem == newItem
        }
    }
}
