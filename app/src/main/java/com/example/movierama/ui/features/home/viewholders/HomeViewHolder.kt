package com.example.movierama.ui.features.home.viewholders

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.model.Movie
import com.example.movierama.ui.features.home.HomeMovieTypeList

interface HomeViewHolder<T: RecyclerView.ViewHolder>{

    val adapter: ListAdapter<Movie, T>

    fun bind(selectedList: HomeMovieTypeList, position: Int)

    fun addMovies(newMovies: List<Movie>)
}