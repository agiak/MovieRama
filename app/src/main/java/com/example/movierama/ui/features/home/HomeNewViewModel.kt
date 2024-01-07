package com.example.movierama.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.model.Movie
import com.example.movierama.model.MoviesType
import com.example.movierama.model.paging.PagingData
import com.example.movierama.model.remote.movies.MoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeNewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val fetchMoviesUseCase: FetchMoviesUseCase,
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    private val pagingData = HomeData()

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            emitLoading()

            val popularResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.POPULAR, 1) }
            val upcomingResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.UPCOMING, 1) }
            val topRatedResponse = async { fetchMoviesUseCase.fetchMovies(MoviesType.TOP_RATED, 1) }
            val nowPlayingResponse =
                async { fetchMoviesUseCase.fetchMovies(MoviesType.NOW_PLAYING, 1) }

            val result =
                awaitAll(popularResponse, upcomingResponse, topRatedResponse, nowPlayingResponse)

            Timber.d("result was $result")

            setPagingData(MoviesType.POPULAR, result[0])
            setPagingData(MoviesType.UPCOMING, result[1])
            setPagingData(MoviesType.TOP_RATED, result[2])
            setPagingData(MoviesType.NOW_PLAYING, result[3])

            _homeState.value = HomeState.MoviesData(
                popular = pagingData.popular.movies,
                nowPlaying = pagingData.nowPlaying.movies,
                topRated = pagingData.topRated.movies,
                upcoming = pagingData.upcoming.movies,
            )
        }
    }

    private fun emitLoading(moviesType: MoviesType? = null) {
        _homeState.value = when (moviesType) {
            MoviesType.POPULAR -> HomeState.UpcomingData(pagingData.popular.movies, true)
            MoviesType.NOW_PLAYING -> HomeState.UpcomingData(pagingData.nowPlaying.movies, true)
            MoviesType.TOP_RATED -> HomeState.UpcomingData(pagingData.topRated.movies, true)
            MoviesType.UPCOMING -> HomeState.UpcomingData(pagingData.upcoming.movies, true)
            null -> HomeState.Loading
        }
    }

    fun fetchMore(moviesType: MoviesType) {
        Timber.d("fetch was called for $moviesType")
        if (moviesType.isFetching()) return
        if (!moviesType.canFetchMore()) return

        viewModelScope.launch {
            emitLoading(moviesType)
            val page = moviesType.getNextPage()
            Timber.d("fetched page $page")
            val moviesResponse =
                fetchMoviesUseCase.fetchMovies(moviesType, page)
            setPagingData(moviesType, moviesResponse)
            updateState(moviesType)
        }
    }

    private fun MoviesType.canFetchMore() =
        with(pagingData) {
            when (this@canFetchMore) {
                MoviesType.POPULAR -> popular.canFetchMore
                MoviesType.NOW_PLAYING -> nowPlaying.canFetchMore
                MoviesType.TOP_RATED -> topRated.canFetchMore
                MoviesType.UPCOMING -> upcoming.canFetchMore
            }
        }

    private fun MoviesType.isFetching() =
        with(pagingData) {
            when (this@isFetching) {
                MoviesType.POPULAR -> popular.isFetching
                MoviesType.NOW_PLAYING -> nowPlaying.isFetching
                MoviesType.TOP_RATED -> topRated.isFetching
                MoviesType.UPCOMING -> upcoming.isFetching
            }
        }


    private fun setPagingData(moviesType: MoviesType, moviesResponse: MoviesResponse) {
        when (moviesType) {
            MoviesType.POPULAR -> {
                pagingData.popular.setPagingData(
                    moviesResponse.totalPages,
                    moviesResponse.getUiMovies()
                )
            }

            MoviesType.NOW_PLAYING -> {
                pagingData.nowPlaying.setPagingData(
                    moviesResponse.totalPages,
                    moviesResponse.getUiMovies()
                )
            }

            MoviesType.TOP_RATED -> {
                pagingData.topRated.setPagingData(
                    moviesResponse.totalPages,
                    moviesResponse.getUiMovies()
                )
            }

            MoviesType.UPCOMING -> {
                pagingData.upcoming.setPagingData(
                    moviesResponse.totalPages,
                    moviesResponse.getUiMovies()
                )
            }
        }
        Timber.d("After setting pagingDatat we have $pagingData")
    }

    private fun updateState(moviesType: MoviesType) {
        _homeState.value =
            when (moviesType) {
                MoviesType.POPULAR -> HomeState.PopularData(
                    movies = pagingData.popular.movies,
                    isLoading = false
                )

                MoviesType.NOW_PLAYING -> HomeState.NowPlayingData(
                    movies = pagingData.nowPlaying.movies,
                    isLoading = false
                )

                MoviesType.TOP_RATED -> HomeState.TopRatedData(
                    movies = pagingData.topRated.movies,
                    isLoading = false
                )

                MoviesType.UPCOMING -> HomeState.UpcomingData(
                    movies = pagingData.upcoming.movies,
                    isLoading = false
                )
            }
    }

    private fun MoviesType.getNextPage(): Int =
        with(pagingData) {
            when (this@getNextPage) {
                MoviesType.POPULAR -> {
                    popular++
                    popular.currentPage
                }

                MoviesType.NOW_PLAYING -> {
                    nowPlaying++
                    nowPlaying.currentPage
                }

                MoviesType.TOP_RATED -> {
                    topRated++
                    topRated.currentPage
                }

                MoviesType.UPCOMING -> {
                    upcoming++
                    upcoming.currentPage
                }
            }
        }
}

sealed class HomeState {
    object Loading : HomeState()
    data class PopularData(val movies: List<Movie>, val isLoading: Boolean = false) : HomeState()
    data class NowPlayingData(val movies: List<Movie>, val isLoading: Boolean = false) : HomeState()
    data class TopRatedData(val movies: List<Movie>, val isLoading: Boolean = false) : HomeState()
    data class UpcomingData(val movies: List<Movie>, val isLoading: Boolean = false) : HomeState()
    data class MoviesData(
        val popular: List<Movie> = emptyList(),
        val nowPlaying: List<Movie> = emptyList(),
        val topRated: List<Movie> = emptyList(),
        val upcoming: List<Movie> = emptyList(),
    ) : HomeState()
}

private data class HomeData(
    var popular: HomePagingData = HomePagingData(),
    var nowPlaying: HomePagingData = HomePagingData(),
    var topRated: HomePagingData = HomePagingData(),
    var upcoming: HomePagingData = HomePagingData()
)

private data class HomePagingData(
    var pagingData: PagingData<Movie> = PagingData(),
    var isLoading: Boolean = false
) {
    val movies: List<Movie>
        get() = pagingData.currentMoviesList.toList()

    val canFetchMore: Boolean
        get() = pagingData.hasMorePagesToFetch()

    val currentPage: Int
        get() = pagingData.currentPage

    operator fun inc(): HomePagingData {
        pagingData++
        return this
    }

    val isFetching: Boolean
        get() = isLoading

    fun setPagingData(totalPages: Int, newItems: List<Movie>) {
        pagingData.apply {
            this.totalPages = totalPages
            currentMoviesList.addAll(newItems)
        }
    }
}
