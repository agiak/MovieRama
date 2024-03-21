package com.example.movierama.features.details.domain

import com.example.movierama.features.details.data.CreditsResponse
import com.example.movierama.features.details.data.MovieDetailsResponse
import com.example.movierama.features.details.data.ReviewsResponse
import com.example.movierama.features.details.data.SimilarResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsService {

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