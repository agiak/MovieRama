package com.example.movierama.features.home.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.features.home.data.HomeItemActions
import com.example.movierama.features.home.data.HomeMovieTypeList
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolder
import com.example.movierama.features.home.presentation.viewholders.nowplaying.NowPlayingViewHolder
import com.example.movierama.features.home.presentation.viewholders.popular.PopularViewHolder
import com.example.movierama.features.home.presentation.viewholders.toprated.TopRatedViewHolder
import com.example.movierama.features.home.presentation.viewholders.upcoming.UpcomingViewHolder
import kotlinx.coroutines.flow.Flow

class HomeMovieTypeAdapter(
    private val actions: HomeItemActions,
    private val lifecycleCoroutineScope: LifecycleCoroutineScope,
    private val moviesTypeData: HashMap<MoviesType, Flow<PagingData<Movie>>>
) : ListAdapter<HomeMovieTypeList, RecyclerView.ViewHolder>(HomeMovieTypeDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return when (viewType) {
            MoviesType.POPULAR.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                PopularViewHolder(
                    binding = binding,
                    actions = actions,
                    lifecycle = lifecycleCoroutineScope,
                    data = moviesTypeData[MoviesType.POPULAR]
                )
            }

            MoviesType.NOW_PLAYING.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                NowPlayingViewHolder(
                    binding = binding,
                    actions = actions,
                    lifecycle = lifecycleCoroutineScope,
                    data = moviesTypeData[MoviesType.NOW_PLAYING]
                )
            }

            MoviesType.TOP_RATED.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                TopRatedViewHolder(
                    binding = binding,
                    actions = actions,
                    lifecycle = lifecycleCoroutineScope,
                    data = moviesTypeData[MoviesType.TOP_RATED]
                )
            }

            MoviesType.UPCOMING.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                UpcomingViewHolder(
                    binding = binding,
                    actions = actions,
                    lifecycle = lifecycleCoroutineScope,
                    data = moviesTypeData[MoviesType.UPCOMING]
                )
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeViewHolder<*>).bind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int =
        getItem(position).moviesType.ordinal

    private class HomeMovieTypeDiffCallback : DiffUtil.ItemCallback<HomeMovieTypeList>() {
        override fun areItemsTheSame(
            oldItem: HomeMovieTypeList,
            newItem: HomeMovieTypeList
        ): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(
            oldItem: HomeMovieTypeList,
            newItem: HomeMovieTypeList
        ): Boolean {
            return oldItem == newItem
        }
    }
}
