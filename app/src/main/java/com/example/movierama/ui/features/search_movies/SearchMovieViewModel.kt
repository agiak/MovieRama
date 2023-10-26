package com.example.movierama.ui.features.search_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.search.SearchRepository
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.model.error_handling.toApiError
import com.example.movierama.model.paging.PagingData
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.search_movie.SearchSuggestion
import com.example.movierama.model.search_movie.SearchedMovie
import com.example.movierama.model.storage.StoredSearchSuggestion
import com.example.movierama.model.toSearchMovie
import com.example.myutils.isNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    private var pagingData = PagingData<SearchedMovie>()
    private var query = SearchFilter()

    init {
        fetchSuggestions()
    }

    private fun fetchSuggestions() {
        viewModelScope.launch {
            runCatching {
                searchRepository.fetchSearchHistory()
            }.onSuccess { searchHistory ->
                _state.update {
                    it.copy(
                        suggestions = searchHistory.toSearchHistory()
                    )
                }
            }
        }
    }

    fun onSearchTyped(input: String) {
        emitLoading()
        pagingData.reset()
        query = input.toSearchFilter()
        saveQuery(query)
        fetchMovies()
    }

    private fun saveQuery(query: SearchFilter) {
        viewModelScope.launch {
            searchRepository.saveSearch(query = query.toStoredSearchSuggestion())
        }
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            runCatching {
                moviesRepository.searchMovies(
                    page = pagingData.currentPage,
                    movieName = query.movieName,
                    year = query.year
                )
            }.onSuccess {
                handleMovieResponse(it)
            }.onFailure {
                handleError(it as Exception)
            }
        }
    }

    fun fetchMore() {
        // Check if we are already fetching movies. In that case we need to skip that call of the function
        if (state.value.isLoadingMore) return

        if (pagingData.hasMorePagesToFetch().not()) return
        pagingData++

        fetchMovies()
    }

    private fun handleError(ex: Exception) {
        _state.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                error = ex.toApiError()
            )
        }
    }

    private fun handleMovieResponse(response: MoviesResponse) {
        pagingData.totalPages = response.totalPages
        pagingData.currentMoviesList.addAll(response.getSearchResults())
        _state.update { state ->
            state.copy(
                movies = pagingData.currentMoviesList.toList(),
                isLoading = false,
                isLoadingMore = false,
                error = null,
                query = query.value
            )
        }
    }

    private fun emitLoading(isLoadingMore: Boolean = false) {
        _state.update {
            it.copy(isLoadingMore = isLoadingMore, isLoading = isLoadingMore.not())
        }
    }

    private fun String.toSearchFilter(): SearchFilter =
        when {
            isNumber() -> SearchFilter(year = this)
            else -> SearchFilter(movieName = this)
        }

    private fun MoviesResponse.getSearchResults(): List<SearchedMovie> =
        ArrayList<SearchedMovie>().apply {
            moviesNetwork.forEach { networkMovie ->
                add(networkMovie.toSearchMovie())
            }
        }

    private fun SearchFilter.toStoredSearchSuggestion() =
        StoredSearchSuggestion(searchInput = value, time = System.currentTimeMillis())

    private fun List<StoredSearchSuggestion>.toSearchHistory() =
        ArrayList<SearchSuggestion>().apply {
            this@toSearchHistory.forEach {
                add(SearchSuggestion(query = it.searchInput, date = it.time.toDate()))
            }
        }

    private fun Long.toDate(): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(this)
        return format.format(date)
    }
}

data class SearchState(
    val suggestions: List<SearchSuggestion> = emptyList(),
    val movies: List<SearchedMovie> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: ApiError? = null,
)

data class SearchFilter(
    val movieName: String? = null,
    val year: String? = null,
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()

    val value: String
        get() = movieName ?: year ?: ""
}
