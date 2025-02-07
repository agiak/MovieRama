package com.example.movierama.model

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
    var isFavourite: Boolean,
    val description: String
)
