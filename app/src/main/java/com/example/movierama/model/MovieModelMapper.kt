package com.example.movierama.model

import com.example.movierama.model.remote.movies.MovieNetwork
import com.example.movierama.model.search_movie.SearchedMovie
import com.example.movierama.model.storage.StoredFavouriteMovie
import com.example.movierama.ui.URL_POSTER
import com.example.movierama.ui.utils.mapToDate
import com.example.myutils.roundToTwoDecimal

fun Movie.toStoredFavouriteMovie() = StoredFavouriteMovie(
    id = id,
    title = title,
    poster = poster,
    rating = rating,
    releaseDate = releaseDate
)

fun MovieNetwork.toHomeMovie() = Movie(
    id = this.id,
    title = title,
    rating = (voteAverage / 2.0F).roundToTwoDecimal(), // round the rating to a 5 stars scale
    releaseDate = releaseDate.mapToDate(),
    poster = URL_POSTER + posterPath, // add base image url because server returns only the prefix
    isFavourite = false // by default is false
)

fun MovieNetwork.toSearchMovie() = SearchedMovie(
    id = this.id,
    title = title,
    logo = URL_POSTER + posterPath,
)

fun MovieDetails.toStoredFavouriteMovie() = StoredFavouriteMovie(
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
