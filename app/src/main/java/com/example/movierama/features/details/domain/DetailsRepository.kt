package com.example.movierama.features.details.domain

import com.example.movierama.features.details.data.CreditsResponse
import com.example.movierama.features.details.data.MovieDetailsResponse
import com.example.movierama.features.details.data.ReviewsResponse
import com.example.movierama.features.details.data.SimilarResponse

interface DetailsRepository {

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
}