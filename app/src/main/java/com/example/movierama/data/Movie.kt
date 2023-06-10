package com.example.movierama.data

data class Movie(
    val id: Long,
    val title: String,
    val poster: String,
    val rating: Float,
    val releaseDate: String,
    var isFavourite: Boolean
)

data class MovieDetails(
    val id: Long,
    val title: String,
    val type: String,
    val releaseDate: String,
    val rating: Float,
    val poster: String,
    val isFavourite: Boolean,
    val description: String,
    val author: String,
    val cast: String
)