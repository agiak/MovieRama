package com.example.movierama.features.favourites.domain

import com.example.movierama.features.favourites.data.StoredFavouriteMovie
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun onFavouriteStatusChanged(movie: StoredFavouriteMovie)

    suspend fun isMovieFavourite(movieId: Long): Boolean

    fun fetchFavouriteMovies(): Flow<List<StoredFavouriteMovie>>
}