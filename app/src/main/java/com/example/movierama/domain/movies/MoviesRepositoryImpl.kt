package com.example.movierama.domain.movies

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse
import com.example.movierama.network.services.MoviesService
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class that handles the retrieval and manipulation of movie-related data.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val dispatchersImpl: IDispatchers,
    private val service: MoviesService
) : MoviesRepository {

    override suspend fun getPopularMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getPopularMovies(page = currentPage)
        }

    override suspend fun getTopRatedMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getTopRatedMovies(page = currentPage)
        }

    override suspend fun getUpcomingMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getUpcomingMovies(page = currentPage)
        }

    override suspend fun getNowPlayingMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchersImpl.backgroundThread()) {
            service.getNowPlayingMovies(page = currentPage)
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
}
