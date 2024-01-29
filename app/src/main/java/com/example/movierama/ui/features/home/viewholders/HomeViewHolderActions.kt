package com.example.movierama.ui.features.home.viewholders

import com.example.movierama.model.MoviesType

data class HomeViewHolderActions(
    val onLabelClicked: (label: MoviesType) -> Unit = {},
    val onItemClick: (movieId: Long) -> Unit = {},
    val onFetchingMovies: (movieType: MoviesType) -> Unit = {}
)
