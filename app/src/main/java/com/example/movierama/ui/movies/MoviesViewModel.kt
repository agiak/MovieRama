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

    // Set to store all movies

    private var allMovies: MutableSet<Movie> = mutableSetOf()

    // MovieFilter object to hold search filter parameters

    var searchFilter = MovieFilter()

    private var totalPages = 1
    private var currentPage = 1

    init {
        loadPopularMovies()
    }

    /**
     * Function to load popular movies.
     * Uses viewModelScope to launch a coroutine and updates the homeState accordingly.
     * Handles pagination and exception cases.
     */
    private fun loadPopularMovies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _homeState.value = UIState.InProgress
            if (isRefresh) {
                allMovies.clear()
                totalPages = 1
                currentPage = 1
            }
            try {
                val movies = repository.getPopularMovies(currentPage)
                handleMovieResponse(movies)
            } catch (ex: Exception) {
                _homeState.value = UIState.Error(ex)
            }
        }
    }

    /**
     * Function to search movies based on the provided MovieFilter input.
     * Updates the searchFilter property and loads popular movies if the search filter is empty.
     * Otherwise, performs a search query using the repository and updates the homeState.
     */
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

    /**
     * Function to load more movies when pagination is available.
     * Increments the currentPage and loads popular movies or performs a search query.
     */
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

    /**
    * Function to handle the change in "isFavourite" status of a movie.
    * Calls the repository to update the "isFavourite" status of the movie.
    */
    fun onFavouriteChanged(movieId: Long) {
        repository.onFavouriteChange(movieId)
    }

    /**
     * Extension function to update the "isFavourite" state of each movie element in the list.
     * Iterates through each movie and calls the repository to check if it is a favourite.
     */
    private fun List<Movie>.setIsFavouriteToMovies() {
        forEach {
            it.isFavourite = repository.isMovieFavourite(it.id)
        }
    }

    /**
     * Function to refresh the movie list by loading popular movies again.
     * Clears the allMovies set and resets the pagination parameters.
     */
    fun refresh() {
        loadPopularMovies(isRefresh = true)
    }
}

/**
 * Data class representing the movie filter parameters. Contains optional properties for
 * movieName and year. Provides a function to check if the filter is empty.
 * */
data class MovieFilter(
    val movieName: String? = null,
    val year: String? = null
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()
}