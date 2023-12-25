package com.example.movierama.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.model.Movie
import com.example.movierama.model.MoviesType
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.model.error_handling.toApiError
import com.example.movierama.model.paging.PagingData
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.toStoredFavouriteMovie
import com.example.movierama.ui.features.search_movies.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val fetchMoviesUseCase: FetchMoviesUseCase,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState

    private var searchFilter = SearchFilter()
    private var moviesPagingData = PagingData<Movie>()

    init {
        fetchMovies()
    }

    private fun fetchMovies(isLoadingMore: Boolean = false) {
        viewModelScope.launch {
            emitLoading(isLoadingMore)
            runCatching {
                fetchMoviesUseCase.fetchMovies(
                    homeState.value.moviesType,
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
        // Check if we are already fetching movies. In that case we need to skip that call of the function
        if (homeState.value.isLoadingMore) return

        if (moviesPagingData.hasMorePagesToFetch().not()) return
        moviesPagingData++

        if (searchFilter.isEmpty().not()) {
            searchMovies(isLoadingMore = true)
        } else {
            fetchMovies(isLoadingMore = true)
        }
    }

    fun refresh() {
        moviesPagingData.reset()
        fetchMovies()
    }

    fun onMovieTypeSelected(type: MoviesType) {
        viewModelScope.launch {
            moviesPagingData.reset()
            _homeState.update {
                it.copy(
                    moviesType = type,
                    isLoading = true,
                )
            }
            fetchMovies()
        }
    }

    private fun searchMovies(isLoadingMore: Boolean) {
        emitLoading(isLoadingMore)
        if (!isLoadingMore) moviesPagingData.reset()
        viewModelScope.launch {
            runCatching {
                moviesRepository.searchMovies(
                    page = moviesPagingData.currentPage,
                    movieName = searchFilter.movieName,
                    year = searchFilter.year
                )
            }.onSuccess {
                handleMovieResponse(it)
            }.onFailure {
                handleError(it as Exception)
            }
        }
    }

    private suspend fun handleMovieResponse(response: MoviesResponse) {
        moviesPagingData.totalPages = response.totalPages
        moviesPagingData.currentMoviesList.addAll(response.getUiMovies().also { fetchedMovies ->
            fetchedMovies.setIsFavouriteToMovies() // updates movies list and sets favourite state
        })
        _homeState.update { state ->
            state.copy(
                movies = moviesPagingData.currentMoviesList.toList(),
                isLoading = false,
                isLoadingMore = false,
                error = null,
                moviesType = state.moviesType
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

    // updates isFavourite state of each movie element of the list
    private suspend fun List<Movie>.setIsFavouriteToMovies() {
        forEach { movie ->
            movie.isFavourite = moviesRepository.isMovieFavourite(movie.id)
        }
    }
}

data class HomeUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: ApiError? = null,
    val searchFilter: SearchFilter = SearchFilter(),
    val moviesType: MoviesType = MoviesType.POPULAR,
)
