package com.example.movierama.ui.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.model.Movie
import com.example.movierama.model.toStoredFavouriteMovie
import com.example.movierama.model.toUiMovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: MoviesRepository,
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