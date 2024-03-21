package com.example.movierama.features.home.presentation.viewholders

import com.example.movierama.core.data.movies.MoviesType

data class HomeViewHolderActions(
    val onLabelClicked: (label: MoviesType) -> Unit = {},
    val onItemClick: (movieId: Long) -> Unit = {},
    val onFetchingMovies: (movieType: MoviesType) -> Unit = {}
)
