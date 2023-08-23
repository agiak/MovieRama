package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FavouriteUseCase @Inject constructor(
    private val repository: MoviesRepository
) {

    fun onFavouriteChanged(movieId: Long) {
        repository.onFavouriteChange(movieId)
    }

    fun isMovieFavourite(movieId: Long) = repository.isMovieFavourite(movieId)

}
