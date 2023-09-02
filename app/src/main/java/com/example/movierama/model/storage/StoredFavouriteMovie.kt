package com.example.movierama.model.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class StoredFavouriteMovie(
    @PrimaryKey val id: Long,
    val title: String,
    val poster: String,
    val rating: Float,
    val releaseDate: String
)
