package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.ui.movies.MoviesType
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class FetchMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend fun fetchMovies(type: MoviesType, page: Int): MoviesResponse =
        when (type) {
            MoviesType.POPULAR -> {
                Timber.d("fetch popular at page $page")
                fetchPopularMovies(page)
            }

            MoviesType.NOW_PLAYING -> {
                Timber.d("fetch now playing at page $page")
                fetchNowPlayingMovies(page)
            }

            MoviesType.TOP_RATED -> {
                Timber.d("fetch top rated at page $page")
                fetchTopRatedMovies(page)
            }

            MoviesType.UPCOMING -> {
                Timber.d("fetch upcoming at page $page")
                fetchUpcomingMovies(page)
            }
        }

    private suspend fun fetchPopularMovies(page: Int): MoviesResponse = repository.getPopularMovies(page)

    private suspend fun fetchTopRatedMovies(page: Int): MoviesResponse = repository.getTopRatedMovies(page)

    private suspend fun fetchUpcomingMovies(page: Int): MoviesResponse = repository.getUpcomingMovies(page)

    private suspend fun fetchNowPlayingMovies(page: Int): MoviesResponse = repository.getNowPlayingMovies(page)
}
