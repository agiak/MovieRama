package com.example.movierama.features.search.domain

import com.example.movierama.core.data.movies.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("language") language: String = "en-US",
        @Query("query") movieName: String? = null,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("year") year: String? = null
    ): MoviesResponse
}