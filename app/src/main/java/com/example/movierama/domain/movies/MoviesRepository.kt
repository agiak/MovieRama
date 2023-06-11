package com.example.movierama.domain.movies

import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.data.network.MoviesService
import com.example.movierama.data.network.credits.CreditsResponse
import com.example.movierama.data.network.movies.MovieDetailsResponse
import com.example.movierama.data.network.reviews.ReviewsResponse
import com.example.movierama.data.network.similar.SimilarResponse
import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.storage.PreferenceManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val dispatchersImpl: IDispatchers,
    private val service: MoviesService,
    private val preferenceManager: PreferenceManager
) {

    suspend fun getPopularMovies(
        currentPage: Int,
    ): MoviesResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.getMovies(page = currentPage)
    }

    suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.searchMovies(
            page = page,
            movieName = movieName,
            year = year
        )
    }

    suspend fun getMovie(
        movieId: Long
    ): MovieDetailsResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.getMovieDetails(movieId = movieId)
    }

    suspend fun getReviews(
        movieId: Long,
        currentPage: Int
    ): ReviewsResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.getReviews(movieId = movieId, page = currentPage)
    }

    suspend fun getSimilarMovies(
        movieId: Long,
        currentPage: Int
    ): SimilarResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.getSimilarMovies(movieId = movieId, page = currentPage)
    }

    suspend fun getMovieCredits(
        movieId: Long
    ): CreditsResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.getMovieCredits(movieId = movieId)
    }

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

    fun isMovieFavourite(movieId: Long) =
        preferenceManager.get(PreferenceManager.FAVOURITE_MOVIES_KEY, emptySet<Long>())
            .contains(movieId)
}
