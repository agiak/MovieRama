package com.example.movierama.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.domain.useCases.MoviesTypeResponse
import com.example.movierama.model.MoviesType
import com.example.movierama.model.error_handling.toApiError
import com.example.movierama.model.remote.movies.toUiMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchMoviesUseCase: FetchMoviesUseCase,
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    private val pagingDataSet = hashMapOf(
        MoviesType.POPULAR to HomePagingData(),
        MoviesType.NOW_PLAYING to HomePagingData(),
        MoviesType.TOP_RATED to HomePagingData(),
        MoviesType.UPCOMING to HomePagingData(),
    )

    init {
        fetchAllMoviesTypes()
    }

    private fun fetchAllMoviesTypes() {
        viewModelScope.launch {
            emitLoading()
            runCatching {
                val popularResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.POPULAR, 1) }
                val nowPlayingResponse =
                    async { fetchMoviesUseCase.fetchMovies(MoviesType.NOW_PLAYING, 1) }
                val topRatedResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.TOP_RATED, 1) }
                val upcomingResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.UPCOMING, 1) }

                awaitAll(popularResponse, upcomingResponse, topRatedResponse, nowPlayingResponse)
            }.onSuccess { menuItemsList ->
                menuItemsList.forEach { setPagingData(it) }
                _homeState.value = HomeState.Result(menuItemsList.toHomeTypeList())
            }.onFailure { error ->
                _homeState.value = HomeState.Error((error as Exception).toApiError())
            }
        }
    }

    private fun List<MoviesTypeResponse>.toHomeTypeList(): List<HomeMovieTypeList> = ArrayList<HomeMovieTypeList>().apply {
        this@toHomeTypeList.forEach { response ->
            add(HomeMovieTypeList(
                moviesType = response.type,
                movies = response.moviesNetwork.toUiMovies(),
                label = response.type.description
            ))
        }
    }

    private fun emitLoading() {
        _homeState.value = HomeState.Loading
    }

    fun fetchMore(moviesType: MoviesType) {
        when {
            isFetching(moviesType) -> return
            !canFetchMore(moviesType) -> return
            else -> fetchMoviesByType(moviesType)
        }
    }

    private fun fetchMoviesByType(moviesType: MoviesType) {
        viewModelScope.launch {
            runCatching {
                val page = getNextPage(moviesType)
                fetchMoviesUseCase.fetchMovies(moviesType, page)
            }.onSuccess { moviesResponse ->
                setPagingData(moviesResponse)
                _homeState.value = HomeState.FetchingMore(
                    moviesType = moviesType,
                    movies = moviesResponse.moviesNetwork.toUiMovies()
                )
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private fun canFetchMore(type: MoviesType) = pagingDataSet[type]?.canFetchMore ?: false

    private fun isFetching(type: MoviesType) = pagingDataSet[type]?.isFetching ?: false

    private fun getNextPage(moviesType: MoviesType): Int =
        pagingDataSet[moviesType]?.let {
            it.pagingData++
            it.currentPage
        } ?: 0

    private fun setPagingData(response: MoviesTypeResponse) {
        pagingDataSet[response.type]?.apply {
            this.setPagingData(response.totalPages, response.moviesNetwork.toUiMovies())
        }
    }
}
