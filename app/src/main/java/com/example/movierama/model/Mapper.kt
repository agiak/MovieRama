package com.example.movierama.model

import com.example.movierama.model.storage.StoredFavouriteMovie

fun Movie.toFavouriteMovie() = StoredFavouriteMovie(
    id = id,
    title = title,
    poster = poster,
    rating = rating,
    releaseDate = releaseDate
)

fun MovieDetails.toFavouriteMovie() = StoredFavouriteMovie(
    id = id,
    title = title,
    poster = poster,
    rating = rating,
    releaseDate = releaseDate
)

fun StoredFavouriteMovie.toUiMovie(): Movie = Movie(
    id = id,
    title = title,
    poster = poster,
    rating = rating,
    releaseDate = releaseDate,
    isFavourite = true
)

fun List<StoredFavouriteMovie>.toUiMovieList(): List<Movie> =
    ArrayList<Movie>().apply {
        this@toUiMovieList.forEach {
            add(it.toUiMovie())
        }
    }
