package com.example.movierama.data.network.movies

import com.example.movierama.data.Movie
import com.example.movierama.ui.URL_POSTER
import com.example.movierama.ui.utils.mapToDate
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val moviesNetwork: List<MovieNetwork>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class MovieNetwork(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("id") val id: Long,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val vote_average: Float,
    @SerializedName("vote_count") val vote_count: Int
){
    fun toHomeMovie() = Movie(
        id = this.id,
        title = title,
        rating = vote_average,
        releaseDate = release_date.mapToDate(),
        poster = URL_POSTER + poster_path,
        isFavourite = false
    )
}