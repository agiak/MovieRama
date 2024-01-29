package com.example.movierama.ui.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.ItemHomeListBinding
import com.example.movierama.model.MoviesType
import com.example.movierama.ui.features.home.viewholders.HomeViewHolder
import com.example.movierama.ui.features.home.viewholders.HomeViewHolderActions
import com.example.movierama.ui.features.home.viewholders.nowplaying.NowPlayingViewHolder
import com.example.movierama.ui.features.home.viewholders.popular.PopularViewHolder
import com.example.movierama.ui.features.home.viewholders.toprated.TopRatedViewHolder
import com.example.movierama.ui.features.home.viewholders.upcoming.UpcomingViewHolder

class HomeMovieTypeAdapter(
    private val actions: HomeViewHolderActions,
) : ListAdapter<HomeMovieTypeList, RecyclerView.ViewHolder>(HomeMovieTypeDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return when (viewType) {
            MoviesType.POPULAR.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                PopularViewHolder(binding = binding, actions = actions)
            }
            MoviesType.NOW_PLAYING.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                NowPlayingViewHolder(binding = binding, actions = actions)
            }
            MoviesType.TOP_RATED.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                TopRatedViewHolder(binding = binding, actions = actions)
            }
            MoviesType.UPCOMING.ordinal -> {
                val binding = ItemHomeListBinding.inflate(inflater, parent, false)
                UpcomingViewHolder(binding = binding, actions = actions)
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
        override fun areItemsTheSame(oldItem: HomeMovieTypeList, newItem: HomeMovieTypeList): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: HomeMovieTypeList, newItem: HomeMovieTypeList): Boolean {
            return oldItem == newItem
        }
    }
}
