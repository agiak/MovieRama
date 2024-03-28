package com.example.movierama.storage.domain.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movierama.features.favourites.data.StoredFavouriteMovie

@Database(
    entities = [
        StoredFavouriteMovie::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FavouriteMoviesDatabase : RoomDatabase() {

    abstract fun favouriteMovieDao(): FavouriteMovieDao
}
