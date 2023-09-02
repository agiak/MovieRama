package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.ui.features.home.SearchFilter
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) {

    suspend fun searchMovie(page: Int, searchFilter: SearchFilter): MoviesResponse =
        repository.searchMovies(
            page = page,
            movieName = searchFilter.movieName,
            year = searchFilter.year
        )
}
