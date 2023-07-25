package com.example.movierama.domain.movies

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse
import com.example.movierama.network.MoviesService
import com.example.movierama.storage.PreferenceManager
import com.example.movierama.storage.PreferenceManagerImpl
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class that handles the retrieval and manipulation of movie-related data.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val dispatchersImpl: IDispatchers,
    private val service: MoviesService,
    private val preferenceManager: PreferenceManager
): MoviesRepository {

    override suspend fun getPopularMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovies(page = currentPage)
        }

    override suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse = withContext(dispatchersImpl.backgroundThread()) {
        service.searchMovies(page = page, movieName = movieName, year = year)
    }

    override suspend fun getMovie(movieId: Long): MovieDetailsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovieDetails(movieId = movieId)
        }

    override suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getReviews(movieId = movieId, page = currentPage)
        }

    override suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getSimilarMovies(movieId = movieId, page = currentPage)
        }

    override suspend fun getMovieCredits(movieId: Long): CreditsResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getMovieCredits(movieId = movieId)
        }

    override fun onFavouriteChange(movieId: Long) {
        // Retrieve the set of favourite movies from the preference manager, defaulting to an empty set
        val favouriteMovies = preferenceManager.get(
            PreferenceManagerImpl.FAVOURITE_MOVIES_KEY, emptySet<Long>()
        ).toMutableSet()

        // if movie is contained remove it otherwise add to the set
        if (favouriteMovies.contains(movieId)) {
            favouriteMovies.remove(movieId)
        } else {
            favouriteMovies.add(movieId)
        }

        // Update the favourite movies set in the preference manager
        preferenceManager.put(PreferenceManagerImpl.FAVOURITE_MOVIES_KEY, favouriteMovies)
    }

    override fun isMovieFavourite(movieId: Long): Boolean {
        val favouriteSet =
            preferenceManager.get(PreferenceManagerImpl.FAVOURITE_MOVIES_KEY, emptySet<Long>())
        return favouriteSet.contains(movieId)
    }
}
