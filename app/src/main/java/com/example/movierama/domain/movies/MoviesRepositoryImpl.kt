package com.example.movierama.domain.movies

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse
import com.example.movierama.model.storage.StoredFavouriteMovie
import com.example.movierama.network.services.MoviesService
import com.example.movierama.storage.localdb.FavouriteMovieDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class that handles the retrieval and manipulation of movie-related data.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val dispatchers: IDispatchers,
    private val service: MoviesService,
    private val localDao: FavouriteMovieDao,
) : MoviesRepository {

    override suspend fun getPopularMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getPopularMovies(page = currentPage)
        }

    override suspend fun getTopRatedMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getTopRatedMovies(page = currentPage)
        }

    override suspend fun getUpcomingMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getUpcomingMovies(page = currentPage)
        }

    override suspend fun getNowPlayingMovies(currentPage: Int): MoviesResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getNowPlayingMovies(page = currentPage)
        }

    override suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?,
    ): MoviesResponse = withContext(dispatchers.backgroundThread()) {
        service.searchMovies(page = page, movieName = movieName, year = year)
    }

    override suspend fun getMovie(movieId: Long): MovieDetailsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getMovieDetails(movieId = movieId)
        }

    override suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getReviews(movieId = movieId, page = currentPage)
        }

    override suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getSimilarMovies(movieId = movieId, page = currentPage)
        }

    override suspend fun getMovieCredits(movieId: Long): CreditsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getMovieCredits(movieId = movieId)
        }

    override suspend fun onFavouriteStatusChanged(movie: StoredFavouriteMovie) {
        withContext(dispatchers.backgroundThread()) {
            if (isMovieFavourite(movie.id).not())
                localDao.insertFavoriteMovie(movie)
            else
                localDao.deleteFavoriteMovie(movie)
        }
    }

    override suspend fun isMovieFavourite(movieId: Long): Boolean =
        withContext(dispatchers.backgroundThread()) {
            localDao.isMovieFavorite(movieId)
        }

    override fun fetchFavouriteMovies(): Flow<List<StoredFavouriteMovie>> =
        localDao.getAllFavoriteMovies()
}
