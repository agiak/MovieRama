package com.example.movierama.core.domain.movies

import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.domain.dispatchers.IDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class that handles the retrieval and manipulation of movie-related data.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val dispatchers: IDispatchers,
    private val service: MoviesService,
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
}
