package com.example.movierama.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.core.domain.movies.usecases.FetchMoviesUseCase
import com.example.movierama.core.domain.movies.usecases.MoviesTypeResponse
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.movies.toUiMovies
import com.example.movierama.network.data.ApiError
import com.example.movierama.network.data.toApiError
import com.example.movierama.features.home.data.HomeMovieTypeList
import com.example.movierama.features.home.data.HomePagingData
import com.example.movierama.features.home.data.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
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
            val popularResponse =
                asyncCatching { fetchMoviesUseCase.fetchMovies(MoviesType.POPULAR, 1) }
            val nowPlayingResponse =
                asyncCatching { fetchMoviesUseCase.fetchMovies(MoviesType.NOW_PLAYING, 1) }
            val topRatedResponse =
                asyncCatching { fetchMoviesUseCase.fetchMovies(MoviesType.TOP_RATED, 1) }
            val upcomingResponse =
                asyncCatching { fetchMoviesUseCase.fetchMovies(MoviesType.UPCOMING, 1) }

            val result = awaitAll(popularResponse, upcomingResponse, topRatedResponse, nowPlayingResponse)

            if (result.isSuccess()) {
                result.getSuccessData().apply {
                    this.forEach { setPagingData(it) }
                    _homeState.value = HomeState.Result(this.toHomeTypeList())
                }
            } else {
                _homeState.value = HomeState.Error(result.getErrorData())
            }

        }
    }

    private fun List<MoviesTypeResponse>.toHomeTypeList(): List<HomeMovieTypeList> =
        ArrayList<HomeMovieTypeList>().apply {
            this@toHomeTypeList.forEach { response ->
                add(
                    HomeMovieTypeList(
                        moviesType = response.type,
                        movies = response.moviesNetwork.toUiMovies(),
                        label = response.type.description
                    )
                )
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

    private fun CoroutineScope.asyncCatching(block: suspend () -> MoviesTypeResponse): Deferred<HomeMovieState> =
        async {
            try {
                HomeMovieState.Success(block())
            } catch (e: Exception) {
                HomeMovieState.Error(e.toApiError())
            }
        }

    private fun List<HomeMovieState>.isSuccess(): Boolean = all { it is HomeMovieState.Success }

    private fun List<HomeMovieState>.getSuccessData(): List<MoviesTypeResponse> =
        ArrayList<MoviesTypeResponse>().apply {
            this@getSuccessData.forEach { homeMovieState ->
                this.add((homeMovieState as HomeMovieState.Success).moviesResponse)
            }
        }

    private fun List<HomeMovieState>.getErrorData(): ApiError =
        (find { it is HomeMovieState.Error } as? HomeMovieState.Error)?.error ?: ApiError.Generic

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

private sealed class HomeMovieState {
    data class Success(val moviesResponse: MoviesTypeResponse) : HomeMovieState()
    data class Error(val error: ApiError) : HomeMovieState()
}