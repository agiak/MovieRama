package com.example.movierama.features.favourites.domain

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.favourites.data.StoredFavouriteMovie
import com.example.movierama.storage.domain.localdb.FavouriteMovieDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class FavouriteUseCase @Inject constructor(
    private val favouriteMovieDao: FavouriteMovieDao,
    private val dispatchersImpl: IDispatchers,
) {

    suspend fun onFavouriteChanged(movie: StoredFavouriteMovie) {
        withContext(dispatchersImpl.backgroundThread()) {
            if (isMovieFavourite(movie.id).not())
                favouriteMovieDao.insertFavoriteMovie(movie)
            else
                favouriteMovieDao.deleteFavoriteMovie(movie)
        }
    }

    suspend fun isMovieFavourite(movieId: Long) = favouriteMovieDao.isMovieFavorite(movieId)

    fun fetchFavouriteMovies() = favouriteMovieDao.getAllFavoriteMovies()

}