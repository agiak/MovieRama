package com.example.movierama.domain.movies

import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse

interface MoviesRepository {

    /**
     * Retrieves a list of popular movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getPopularMovies(currentPage: Int): MoviesResponse

    /**
     * Searches for movies based on the provided criteria.
     *
     * @param page The page number.
     * @param movieName The name of the movie to search for.
     * @param year The release year of the movie to search for.
     * @return The response containing the list of search results.
     */
    suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse

    /**
     * Retrieves details for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The response containing the movie details.
     */
    suspend fun getMovie(movieId: Long): MovieDetailsResponse

    /**
     * Retrieves reviews for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @param currentPage The current page number.
     * @return The response containing the list of reviews.
     */
    suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse

    /**
     * Retrieves similar movies for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @param currentPage The current page number.
     * @return The response containing the list of similar movies.
     */
    suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse

    /**
     * Retrieves the credits for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The response containing the movie credits.
     */
    suspend fun getMovieCredits(movieId: Long): CreditsResponse

    /**
     * Handles the change in favorite status for a movie.
     *
     * @param movieId The ID of the movie.
     */
    fun onFavouriteChange(movieId: Long)

    /**
     * Checks if a movie is marked as a favorite.
     *
     * @param movieId The ID of the movie.
     * @return `true` if the movie is marked as a favorite, `false` otherwise.
     */
    fun isMovieFavourite(movieId: Long): Boolean
}
