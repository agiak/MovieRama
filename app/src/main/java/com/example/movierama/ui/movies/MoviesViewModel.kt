package com.example.movierama.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.data.Movie
import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
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

    fun loadPopularMovies() {
        viewModelScope.launch {
            _homeState.value = UIState.InProgress
            try {
                val movies = repository.getPopularMovies(currentPage)
                handleMovieResponse(movies)
            } catch (ex: Exception) {
                _homeState.value = UIState.Error(ex)
            }
        }
    }

    fun searchMovies(input: MovieFilter) {
        searchFilter = input
        if (searchFilter.isEmpty()) {
            allMovies.clear()
            loadPopularMovies()
        } else {
            currentPage = 1
            viewModelScope.launch {
                _homeState.value = UIState.InProgress
                try {
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

    fun loadMoreMovies() {
        if (currentPage < totalPages) {
            currentPage++
            if (searchFilter.isEmpty()) {
                loadPopularMovies()
            } else {
                viewModelScope.launch {
                    _homeState.value = UIState.LoadingMore
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
}

data class MovieFilter(
    val movieName: String? = null,
    val year: String? = null
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()
}