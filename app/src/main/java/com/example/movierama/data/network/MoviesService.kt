package com.example.movierama.data.network

import com.example.movierama.data.network.credits.CreditsResponse
import com.example.movierama.data.network.movies.MovieDetailsResponse
import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.data.network.reviews.ReviewsResponse
import com.example.movierama.data.network.similar.SimilarResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("language") language: String = "en-US",
        @Query("query") movieName: String? = null,
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean = false,
        @Query("year") year: String? = null
    ): MoviesResponse


    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
    ): CreditsResponse


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
    ): ReviewsResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
    ): SimilarResponse
}