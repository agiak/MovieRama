package com.example.movierama.core.domain.movies

import com.example.movierama.core.data.movies.MoviesResponse

interface MoviesRepository {

    /**
     * Retrieves a list of popular movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getPopularMovies(currentPage: Int): MoviesResponse

    /**
     * Retrieves a list of now playing movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getNowPlayingMovies(currentPage: Int): MoviesResponse

    /**
     * Retrieves a list of upcoming movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getUpcomingMovies(currentPage: Int): MoviesResponse

    /**
     * Retrieves a list of top rated movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getTopRatedMovies(currentPage: Int): MoviesResponse
}
