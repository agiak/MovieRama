package com.example.movierama.ui.features.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.domain.useCases.MoviesTypeResponse
import com.example.movierama.model.Movie
import com.example.movierama.model.MoviesType
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.model.error_handling.toApiError
import com.example.movierama.model.paging.PagingData
import com.example.movierama.model.remote.movies.toUiMovies
import com.example.movierama.model.toStoredFavouriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesTypeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val fetchMoviesUseCase: FetchMoviesUseCase,
) : ViewModel() {

    private val _homeState = MutableStateFlow(MoviesTypeState())
    val homeState: StateFlow<MoviesTypeState> = _homeState

    private var moviesPagingData = PagingData<Movie>()

    private var movieType: MoviesType = MoviesType.POPULAR

    fun fetchMovies(moviesType: MoviesType = movieType, isLoadingMore: Boolean = false) {
        viewModelScope.launch {
            emitLoading(isLoadingMore)
            movieType = moviesType
            runCatching {
                fetchMoviesUseCase.fetchMovies(
                    movieType,
                    moviesPagingData.currentPage
                )
            }.onSuccess {
                handleMovieResponse(it)
            }.onFailure {
                handleError(it as Exception)
            }
        }
    }

    private fun handleError(ex: Exception) {
        _homeState.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                error = ex.toApiError()
            )
        }
    }

    fun fetchMore() {
        when {
            homeState.value.isLoadingMore -> return
            !moviesPagingData.canFetchMore() -> return
            else -> {
                moviesPagingData++
                fetchMovies(isLoadingMore = true)
            }
        }
    }

    fun refresh() {
        moviesPagingData.reset()
        fetchMovies(movieType)
    }

    private suspend fun handleMovieResponse(response: MoviesTypeResponse) {
        moviesPagingData.apply {
            totalPages = response.totalPages
            currentMoviesList.addAll(response.moviesNetwork.toUiMovies().also { fetchedMovies ->
                fetchedMovies.setIsFavouriteToMovies() // updates movies list and sets favourite state
            })
        }
        _homeState.update { state ->
            state.copy(
                movies = moviesPagingData.currentMoviesList.toList(),
                isLoading = false,
                isLoadingMore = false,
                error = null,
            )
        }
    }

    private fun emitLoading(isLoadingMore: Boolean = false) {
        _homeState.update {
            it.copy(isLoadingMore = isLoadingMore, isLoading = isLoadingMore.not())
        }
    }

    fun onFavouriteChanged(movie: Movie) {
        viewModelScope.launch {
            moviesRepository.onFavouriteStatusChanged(movie.toStoredFavouriteMovie())
        }
    }

    private suspend fun List<Movie>.setIsFavouriteToMovies() {
        forEach { movie ->
            movie.isFavourite = moviesRepository.isMovieFavourite(movie.id)
        }
    }
}

data class MoviesTypeState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: ApiError? = null
)
