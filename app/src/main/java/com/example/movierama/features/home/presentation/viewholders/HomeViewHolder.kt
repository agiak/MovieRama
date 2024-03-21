package com.example.movierama.features.home.presentation.viewholders

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.features.home.data.HomeMovieTypeList

interface HomeViewHolder<T: RecyclerView.ViewHolder>{

    val adapter: ListAdapter<Movie, T>

    fun bind(selectedList: HomeMovieTypeList, position: Int)

    fun addMovies(newMovies: List<Movie>)
}