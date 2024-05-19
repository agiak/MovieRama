package com.example.movierama.core.domain.movies.usecases

import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.domain.movies.MoviesRepository
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FetchMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend fun execute(type: MoviesType, page: Int): MoviesTypeResponse =
        MoviesTypeResponse(
            when (type) {
                MoviesType.POPULAR -> fetchPopularMovies(page)
                MoviesType.NOW_PLAYING -> fetchNowPlayingMovies(page)
                MoviesType.TOP_RATED -> fetchTopRatedMovies(page)
                MoviesType.UPCOMING -> fetchUpcomingMovies(page)
            },
            type
        )

    private suspend fun fetchPopularMovies(page: Int): MoviesResponse =
        repository.getPopularMovies(page)

    private suspend fun fetchTopRatedMovies(page: Int): MoviesResponse =
        repository.getTopRatedMovies(page)

    private suspend fun fetchUpcomingMovies(page: Int): MoviesResponse =
        repository.getUpcomingMovies(page)

    private suspend fun fetchNowPlayingMovies(page: Int): MoviesResponse =
        repository.getNowPlayingMovies(page)
}


data class MoviesTypeResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val moviesNetwork: List<MovieNetwork>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    val type: MoviesType
) {
    constructor(moviesResponse: MoviesResponse, type: MoviesType) : this(
        page = moviesResponse.page,
        moviesNetwork = moviesResponse.moviesNetwork,
        totalPages = moviesResponse.totalPages,
        totalResults = moviesResponse.totalPages,
        type = type
    )
}