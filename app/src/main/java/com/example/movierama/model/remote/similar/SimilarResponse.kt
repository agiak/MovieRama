package com.example.movierama.model.remote.similar

import com.example.movierama.ui.URL_POSTER
import com.google.gson.annotations.SerializedName

data class SimilarResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val similarMovieNetworks: List<SimilarMovieNetwork>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class SimilarMovieNetwork(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Long,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {
    fun toUiSimilarMovie() = SimilarMovie(
        id = id,
        poster = URL_POSTER + posterPath
    )
}

data class SimilarMovie(
    val id: Long,
    val poster: String
)
