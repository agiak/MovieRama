package com.example.movierama.features.favourites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.toStoredFavouriteMovie
import com.example.movierama.core.data.movies.toUiMovieList
import com.example.movierama.features.favourites.domain.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: FavouriteRepository,
) : ViewModel() {

    val favouriteMovies: StateFlow<List<Movie>> = repository.fetchFavouriteMovies()
        .map { storedMovies -> storedMovies.toUiMovieList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onFavouriteChanged(movie: Movie) {
        viewModelScope.launch {
            repository.onFavouriteStatusChanged(movie.toStoredFavouriteMovie())
        }
    }

}