package com.example.movierama.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.useCases.FavouriteUseCase
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.domain.useCases.SearchMovieUseCase
import com.example.movierama.model.Movie
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCases: MoviesUseCases,
) : ViewModel() {

    private val _homeState = MutableStateFlow<UIState<List<Movie>>>(UIState.IDLE)
    val homeState: StateFlow<UIState<List<Movie>>> = _homeState

    private var allMovies: MutableSet<Movie> = mutableSetOf()

    private var searchFilter = SearchFilter()

    private var movieType: MoviesType = MoviesType.POPULAR

    private var totalPages = 1
    private var currentPage = 1

    init {
        fetchMovies()
    }

    private fun fetchMovies(isLoadingMore: Boolean = false) {
        viewModelScope.launch {
            _homeState.value = if (isLoadingMore) UIState.LoadingMore else UIState.InProgress
            Timber.d("fetchMovies was called")

            try {
                Timber.d("fetch movies with page $currentPage")
                val movies = useCases.fetchMoviesUseCase.fetchMovies(movieType, currentPage)
                handleMovieResponse(movies)
            } catch (ex: Exception) {
                _homeState.value = UIState.Error(ex)
            }
        }
    }

    fun fetchMoreMovies() {
        Timber.d("Fetch more was called with state ${homeState.value}")
        // Check if we are already fetching movies. In that case we need to skip that call of the function
        if (homeState.value is UIState.LoadingMore) {
            Timber.d("Skip fetching because currently loading/fetching...")
            return
        }

        // Check if there are more pages to load
        // In case there are more pages to load increase the page.
        // Note that afterwards we need to set the state to LoadingMore.
        // Otherwise just return
        if (currentPage >= totalPages) return
        currentPage++
        Timber.d("Increased current page to $currentPage and set state to LoadingMore")

        // Check if search filter is still exists
        // If we have search filter we need to call search endpoint
        // Otherwise we need to fetch more movies by the same type
        if (searchFilter.isEmpty().not()) {
            searchMovies(searchFilter, isLoadingMore = true)
        } else {
            Timber.d("fetch more movies with page $currentPage")
            fetchMovies(isLoadingMore = true)
        }
    }

    fun refresh() {
        resetMoviesAndPages()
        fetchMovies()
    }

    fun onMovieTypeSelected(type: MoviesType) {
        movieType = type
        resetMoviesAndPages()
        fetchMovies()
    }

    private fun resetMoviesAndPages() {
        allMovies.clear()
        totalPages = 1
        currentPage = 1
    }

    fun searchMovies(input: SearchFilter, isLoadingMore: Boolean = false) {
        _homeState.value = if (isLoadingMore) UIState.LoadingMore else UIState.InProgress
        searchFilter = input
        resetMoviesAndPages()
        if (searchFilter.isEmpty()) {
            // if search filter is empty reset list and pages and just retrieve movies for the last
            // selected type
            fetchMovies()
        } else {
            viewModelScope.launch {
                try {
                    Timber.d("search with movie name '${searchFilter.movieName}' and page $currentPage")
                    val response =
                        useCases.searchMovieUseCase.searchMovie(currentPage, searchFilter)
                    handleMovieResponse(response)
                } catch (ex: Exception) {
                    _homeState.value = UIState.Error(ex)
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
        useCases.favouriteUseCase.onFavouriteChanged(movieId)
    }

    // updates isFavourite state of each movie element of the list
    private fun List<Movie>.setIsFavouriteToMovies() {
        forEach { movie ->
            movie.isFavourite = useCases.favouriteUseCase.isMovieFavourite(movie.id)
        }
    }

}

data class SearchFilter(
    val movieName: String? = null,
    val year: String? = null
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()
}

enum class MoviesType(val description: String) {
    POPULAR("Popular"),
    NOW_PLAYING("Now playing"),
    TOP_RATED("Top rated"),
    UPCOMING("Upcoming")
}

data class MoviesUseCases(
    val fetchMoviesUseCase: FetchMoviesUseCase,
    val searchMovieUseCase: SearchMovieUseCase,
    val favouriteUseCase: FavouriteUseCase
)
