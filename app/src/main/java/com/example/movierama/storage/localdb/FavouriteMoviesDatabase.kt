package com.example.movierama.storage.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movierama.model.storage.StoredFavouriteMovie

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
