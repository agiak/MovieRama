package com.example.movierama.ui.features.home.viewholders.toprated

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.model.Movie
import com.example.movierama.ui.features.home.HomeMovieTypeList
import com.example.movierama.ui.features.home.viewholders.HomeViewHolder
import com.example.movierama.ui.features.home.viewholders.HomeViewHolderActions
import com.example.movierama.ui.utils.addOnLoadMoreListener

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
