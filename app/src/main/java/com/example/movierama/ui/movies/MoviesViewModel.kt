package com.example.movierama.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.data.Movie
import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.ui.UIState
import com.example.movierama.ui.utils.DebounceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val debounceUtil: DebounceUtil
) : ViewModel() {

    private val _homeState = MutableStateFlow<UIState<List<Movie>>>(UIState.IDLE)
    val homeState: StateFlow<UIState<List<Movie>>> = _homeState

    private var allMovies: MutableSet<Movie> = mutableSetOf()

    var searchFilter = MovieFilter()

    private var totalPages = 1
    private var currentPage = 1

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _homeState.value = UIState.InProgress
            Timber.d("loadPopularMovies was called and isRefresh $isRefresh")
            if (isRefresh){
                allMovies.clear()
                totalPages = 1
                currentPage = 1
            }
            try {
                Timber.d("loadPopularMovies with page $currentPage")
                val movies = repository.getPopularMovies(currentPage)
                handleMovieResponse(movies)
            } catch (ex: Exception) {
                _homeState.value = UIState.Error(ex)
            }
        }
    }

    fun loadMorePopularMovies() {
        viewModelScope.launch {
            _homeState.value = UIState.LoadingMore
            try {
                Timber.d("loadMorePopularMovies for page $currentPage")
                val movies = repository.getPopularMovies(currentPage)
                handleMovieResponse(movies)
            } catch (ex: Exception) {
                _homeState.value = UIState.Error(ex)
            }
        }
    }

    fun searchMovies(input: MovieFilter) {
        _homeState.value = UIState.InProgress
        debounceUtil.debounce {
            searchFilter = input
            if (searchFilter.isEmpty()) {
                allMovies.clear()
                loadPopularMovies()
            } else {
                currentPage = 1
                viewModelScope.launch {
                    try {
                        Timber.d("search with movie name '${searchFilter.movieName}' and page $currentPage")
                        val response = repository.searchMovies(
                            page = currentPage,
                            movieName = searchFilter.movieName,
                            year = searchFilter.year
                        )
                        allMovies.clear()
                        handleMovieResponse(response)
                    } catch (ex: Exception) {
                        _homeState.value = UIState.Error(ex)
                    }
                }
            }
        }
    }

    fun loadMoreMovies() {
        Timber.d("Load more was called")
        if (homeState.value is UIState.LoadingMore) {
            Timber.d("Skip loading because currently loading...")
            return
        }
        if (currentPage < totalPages) {
            currentPage++
            _homeState.value = UIState.LoadingMore
            if (searchFilter.isEmpty()) {
                loadMorePopularMovies()
            } else {
                viewModelScope.launch {
                    try {
                        val movies = repository.searchMovies(
                            page = currentPage,
                            movieName = searchFilter.movieName,
                            year = searchFilter.year
                        )
                        handleMovieResponse(movies)
                    } catch (ex: Exception) {
                        _homeState.value = UIState.Error(ex)
                    }
                }
            }
        }
    }

    private fun handleMovieResponse(response: MoviesResponse) {
        totalPages = response.totalPages
        allMovies.addAll(response.getUiMovies().also {
            it.setIsFavouriteToMovies()
        })
        Timber.e("Finally retrieved ${response.moviesNetwork.size} network movies for page ${response.page}")
        Timber.e("Submitted to UI ${allMovies.size} movies")
        _homeState.value = UIState.Result(allMovies.toList())
    }

    fun onFavouriteChanged(movieId: Long) {
        repository.onFavouriteChange(movieId)
    }

    // updates isFavourite state of each movie element of the list
    private fun List<Movie>.setIsFavouriteToMovies() {
        forEach {
            it.isFavourite = repository.isMovieFavourite(it.id)
        }
    }

    fun refresh() {
        loadPopularMovies(isRefresh = true)
    }
}

data class MovieFilter(
    val movieName: String? = null,
    val year: String? = null
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()
}