package com.example.movierama.features.home.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.features.home.data.HomeMovieTypeList

interface HomeViewHolder<T: RecyclerView.ViewHolder>{
    fun bind(selectedList: HomeMovieTypeList, position: Int)
}