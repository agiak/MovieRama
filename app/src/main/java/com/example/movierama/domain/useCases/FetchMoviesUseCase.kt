package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.model.MoviesType
import com.example.movierama.model.remote.movies.MoviesResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FetchMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend fun fetchMovies(type: MoviesType, page: Int): MoviesResponse =
        when (type) {
            MoviesType.POPULAR -> {
                fetchPopularMovies(page)
            }

            MoviesType.NOW_PLAYING -> {
                fetchNowPlayingMovies(page)
            }

            MoviesType.TOP_RATED -> {
                fetchTopRatedMovies(page)
            }

            MoviesType.UPCOMING -> {
                fetchUpcomingMovies(page)
            }
        }

    private suspend fun fetchPopularMovies(page: Int): MoviesResponse = repository.getPopularMovies(page)

    private suspend fun fetchTopRatedMovies(page: Int): MoviesResponse = repository.getTopRatedMovies(page)

    private suspend fun fetchUpcomingMovies(page: Int): MoviesResponse = repository.getUpcomingMovies(page)

    private suspend fun fetchNowPlayingMovies(page: Int): MoviesResponse = repository.getNowPlayingMovies(page)
}
