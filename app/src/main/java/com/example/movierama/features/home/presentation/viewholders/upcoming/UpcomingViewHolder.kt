package com.example.movierama.features.home.presentation.viewholders.upcoming

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.myutils.hide
import com.example.common.myutils.show
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.features.home.data.HomeItemActions
import com.example.movierama.features.home.data.HomeMovieTypeList
import com.example.movierama.features.home.presentation.viewholders.HomeLoadStateAdapter
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpcomingViewHolder(
    private val binding: ItemHomeListBinding,
    private val actions: HomeItemActions,
    private val lifecycle: LifecycleCoroutineScope,
    private val data: Flow<PagingData<Movie>>?,
) : RecyclerView.ViewHolder(binding.root), HomeViewHolder<UpcomingPagingAdapter.MovieViewHolder> {

    private val pagingAdapter: UpcomingPagingAdapter =
        UpcomingPagingAdapter { actions.onItemClick(it) }

    override fun bind(selectedList: HomeMovieTypeList, position: Int) {
        with(selectedList) {
            binding.label.apply {
                text = label
                setOnClickListener { actions.onLabelClicked(moviesType) }
            }

            binding.list.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
                this.adapter =
                    pagingAdapter.withLoadStateFooter(HomeLoadStateAdapter(retry = { pagingAdapter.refresh() }))
                pagingAdapter.addLoadStateListener {
                    when(it.source.refresh) {
                        is LoadState.Error -> {}
                        LoadState.Loading -> binding.loader.show()
                        is LoadState.NotLoading -> binding.loader.hide()
                    }
                }
            }

            lifecycle.launch {
                data?.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }
    }
}
