package com.example.movierama.ui.features.home_new

import com.example.movierama.model.Movie
import com.example.movierama.model.MoviesType

data class HomeList(
    val label: String,
    val movies: List<Movie> = emptyList(),
    val moviesType: MoviesType,
)
