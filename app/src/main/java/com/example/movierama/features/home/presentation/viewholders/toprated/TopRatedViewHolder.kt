package com.example.movierama.features.home.presentation.viewholders.toprated

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.PagingData
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
import timber.log.Timber

class TopRatedViewHolder(
    private val binding: ItemHomeListBinding,
    private val actions: HomeItemActions,
    private val lifecycle: LifecycleCoroutineScope,
    private val data: Flow<PagingData<Movie>>?,
) : RecyclerView.ViewHolder(binding.root), HomeViewHolder<TopRatedPagingAdapter.MovieViewHolder> {

    private val pagingAdapter: TopRatedPagingAdapter = TopRatedPagingAdapter { actions.onItemClick(it) }

    override fun bind(selectedList: HomeMovieTypeList, position: Int) {
        with(selectedList) {
            binding.label.apply {
                text = label
                setOnClickListener { actions.onLabelClicked(moviesType) }
            }

            binding.list.apply {
                this.adapter =
                    pagingAdapter.withLoadStateFooter(HomeLoadStateAdapter(retry = { pagingAdapter.refresh() }))
                pagingAdapter.addLoadStateListener {
                    when(it.source.refresh) {
                        is LoadState.Error -> {

                        }
                        LoadState.Loading -> binding.loader.show()
                        is LoadState.NotLoading -> binding.loader.hide()
                    }
                }
            }

            lifecycle.launch {
                data?.collectLatest {
                    Timber.d("submit top rated data")
                    pagingAdapter.submitData(it)
                }
            }
        }
    }
}
