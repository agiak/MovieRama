package com.example.movierama.features.home.presentation.viewholders.upcoming

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.data.HomeMovieTypeList
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolder
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolderActions
import com.example.movierama.core.presentation.utils.addOnLoadMoreListener

class UpcomingViewHolder(
    private val binding: ItemHomeListBinding,
    private val actions: HomeViewHolderActions,
) : RecyclerView.ViewHolder(binding.root), HomeViewHolder<UpcomingAdapter.MovieViewHolder> {

    override val adapter: ListAdapter<Movie, UpcomingAdapter.MovieViewHolder> = UpcomingAdapter(onClick = {
        actions.onItemClick(it)
    })

    override fun bind(selectedList: HomeMovieTypeList, position: Int) {
        with(selectedList) {
            binding.label.apply {
                text = label
                setOnClickListener { actions.onLabelClicked(moviesType) }
            }

            binding.list.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
                this.adapter = this@UpcomingViewHolder.adapter
                this@UpcomingViewHolder.adapter.submitList(movies)
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
