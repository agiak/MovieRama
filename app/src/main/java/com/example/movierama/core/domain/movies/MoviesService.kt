package com.example.movierama.core.domain.movies

import com.example.movierama.features.details.data.CreditsResponse
import com.example.movierama.features.details.data.MovieDetailsResponse
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.features.details.data.ReviewsResponse
import com.example.movierama.features.details.data.SimilarResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesResponse
}
