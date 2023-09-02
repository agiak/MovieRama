package com.example.movierama.model.remote.movies

import com.example.movierama.model.MovieDetails
import com.example.movierama.ui.URL_POSTER
import com.example.movierama.ui.utils.mapToDate
import com.example.myutils.roundToTwoDecimal
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("belongs_to_collection") val belongs_to_collection: Any,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("imdb_id") val imdb_id: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("production_companies") val production_companies: List<ProductionCompany>,
    @SerializedName("production_countries") val production_countries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("spoken_languages") val spoken_languages: List<SpokenLanguage>,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("vote_count") val votes: Int
){
    fun toMovieDetails() = MovieDetails(
        id = id,
        title = title,
        type = getType(),
        releaseDate = releaseDate.mapToDate(),
        rating = (rating / 2.0F).roundToTwoDecimal(),
        poster = URL_POSTER + posterPath,
        isFavourite = false,
        description = overview
    )

    private fun getType(): String {
        return genres.joinToString(", ") { it.name }
    }
}

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class ProductionCompany(
    @SerializedName("id") val id: Int,
    @SerializedName("logo_path") val logo_path: String,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val origin_country: String
)

data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso_3166_1: String,
    @SerializedName("name") val name: String
)

data class SpokenLanguage(
     @SerializedName("english_name") val english_name: String,
     @SerializedName("iso_639_1") val iso_639_1: String,
     @SerializedName("name") val name: String
)
