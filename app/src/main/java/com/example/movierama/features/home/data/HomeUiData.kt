package com.example.movierama.features.home.data

import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MoviesType


data class HomeMovieTypeList(
    val label: String,
    val movies: List<Movie> = emptyList(),
    val moviesType: MoviesType,
)

data class HomeItemActions(
    val onLabelClicked: (label: MoviesType) -> Unit = {},
    val onItemClick: (movieId: Long) -> Unit = {},
)