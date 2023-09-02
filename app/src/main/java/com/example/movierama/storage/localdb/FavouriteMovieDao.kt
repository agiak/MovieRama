package com.example.movierama.storage.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movierama.model.storage.StoredFavouriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMovieDao {

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavoriteMovies(): Flow<List<StoredFavouriteMovie>>

    @Insert
    suspend fun insertFavoriteMovie(movie: StoredFavouriteMovie)

    @Delete
    suspend fun deleteFavoriteMovie(movie: StoredFavouriteMovie)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_movies WHERE id = :movieId LIMIT 1)")
    suspend fun isMovieFavorite(movieId: Long): Boolean
}
