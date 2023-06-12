package com.example.movierama.domain.movies

import com.example.movierama.data.network.MoviesService
import com.example.movierama.data.network.credits.CreditsResponse
import com.example.movierama.data.network.movies.MovieDetailsResponse
import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.data.network.reviews.ReviewsResponse
import com.example.movierama.data.network.similar.SimilarResponse
import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.storage.PreferenceManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class that handles the retrieval and manipulation of movie-related data.
 */
class MoviesRepository @Inject constructor(
    private val dispatchersImpl: IDispatchers,
    private val service: MoviesService,
    private val preferenceManager: PreferenceManager
) {

    /**
     * Retrieves a list of popular movies from the server.
     *
     * @param currentPage The current page number.
     * @return The response containing the list of popular movies.
     */
    suspend fun getPopularMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovies(page = currentPage)
        }

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
    ): MoviesResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.searchMovies(page = page, movieName = movieName, year = year)
    }

    /**
     * Retrieves details for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The response containing the movie details.
     */
    suspend fun getMovie(movieId: Long): MovieDetailsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovieDetails(movieId = movieId)
        }

    /**
     * Retrieves reviews for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @param currentPage The current page number.
     * @return The response containing the list of reviews.
     */
    suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getReviews(movieId = movieId, page = currentPage)
        }

    /**
     * Retrieves similar movies for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @param currentPage The current page number.
     * @return The response containing the list of similar movies.
     */
    suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getSimilarMovies(movieId = movieId, page = currentPage)
        }

    /**
     * Retrieves the credits for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The response containing the movie credits.
     */
    suspend fun getMovieCredits(movieId: Long): CreditsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovieCredits(movieId = movieId)
        }

    /**
     * Handles the change in favorite status for a movie.
     *
     * @param movieId The ID of the movie.
     */
    fun onFavouriteChange(movieId: Long) {
        // Retrieve the set of favourite movies from the preference manager, defaulting to an empty set
        val favouriteMovies = preferenceManager.get(
            PreferenceManager.FAVOURITE_MOVIES_KEY, emptySet<Long>()
        ).toMutableSet()

        // Remove the movieId from the set and determine if it was previously present
        val isMovieFavourite = favouriteMovies.remove(movieId).not()

        // If the movie was not previously a favourite, add it to the set
        if (isMovieFavourite) {
            favouriteMovies.add(movieId)
        }

        // Update the favourite movies set in the preference manager
        preferenceManager.put(PreferenceManager.FAVOURITE_MOVIES_KEY, favouriteMovies)
    }

    /**
     * Checks if a movie is marked as a favorite.
     *
     * @param movieId The ID of the movie.
     * @return `true` if the movie is marked as a favorite, `false` otherwise.
     */
    fun isMovieFavourite(movieId: Long): Boolean =
        preferenceManager.get(PreferenceManager.FAVOURITE_MOVIES_KEY, emptySet<Long>())
            .contains(movieId)
}
