package com.example.movierama.features.favourites.domain

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.domain.storage.localdb.FavouriteMovieDao
import com.example.movierama.features.favourites.data.StoredFavouriteMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dispatchers: IDispatchers,
    private val localDao: FavouriteMovieDao,
) : FavouriteRepository {

    override suspend fun onFavouriteStatusChanged(movie: StoredFavouriteMovie) {
        withContext(dispatchers.backgroundThread()) {
            if (isMovieFavourite(movie.id).not())
                localDao.insertFavoriteMovie(movie)
            else
                localDao.deleteFavoriteMovie(movie)
        }
    }

    override suspend fun isMovieFavourite(movieId: Long): Boolean =
        withContext(dispatchers.backgroundThread()) {
            localDao.isMovieFavorite(movieId)
        }

    override fun fetchFavouriteMovies(): Flow<List<StoredFavouriteMovie>> =
        localDao.getAllFavoriteMovies()
}
