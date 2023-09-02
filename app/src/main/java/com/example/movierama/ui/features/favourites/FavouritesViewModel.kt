package com.example.movierama.ui.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.useCases.favourites.FavouriteUseCase
import com.example.movierama.model.Movie
import com.example.movierama.model.toFavouriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouriteUseCase: FavouriteUseCase
) : ViewModel() {

    fun getFavouriteMovies() = favouriteUseCase.fetchFavouriteMovies()

    fun onFavouriteChanged(movie: Movie) {
        viewModelScope.launch {
            favouriteUseCase.onFavouriteChanged(movie.toFavouriteMovie())
        }
    }

}
