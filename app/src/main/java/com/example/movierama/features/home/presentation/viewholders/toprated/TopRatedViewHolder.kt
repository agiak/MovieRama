package com.example.movierama.features.home.presentation.viewholders.toprated

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.data.HomeMovieTypeList
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolder
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolderActions
import com.example.movierama.core.presentation.utils.addOnLoadMoreListener

class TopRatedViewHolder(
    private val binding: ItemHomeListBinding,
    private val actions: HomeViewHolderActions,
) : RecyclerView.ViewHolder(binding.root), HomeViewHolder<TopRatedAdapter.MovieViewHolder> {

    override val adapter: ListAdapter<Movie, TopRatedAdapter.MovieViewHolder> = TopRatedAdapter(onClick = {
        actions.onItemClick(it)
    })

    override fun bind(selectedList: HomeMovieTypeList, position: Int) {
        with(selectedList) {
            binding.label.apply {
                text = label
                setOnClickListener { actions.onLabelClicked(moviesType) }
            }

            binding.list.apply {
                this.adapter = this@TopRatedViewHolder.adapter
                this@TopRatedViewHolder.adapter.submitList(movies)
                addOnLoadMoreListener { actions.onFetchingMovies(moviesType) }
            }
        }
    }

    override fun addMovies(newMovies: List<Movie>) {
        val currentList = adapter.currentList.toMutableList()
        currentList.addAll(newMovies)
        adapter.submitList(currentList)
    }
}
