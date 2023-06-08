package com.example.movierama.ui.movies

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.data.Movie
import com.example.movierama.data.network.EmptyResponseDataException
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

    private val _movies = MutableStateFlow<UIState<List<Movie>>>(UIState.IDLE)
    val movies: StateFlow<UIState<List<Movie>>> = _movies

    var layoutManagerState: Parcelable? = null

    private var allMovies: MutableList<Movie> = mutableListOf()

    private var totalPages = 1
    private var currentPage = 1

    fun getMoviesPerPage(resetPages: Boolean = false): Boolean {
        _movies.value = UIState.InProgress

        if (resetPages) {
            currentPage = 1
            allMovies = mutableListOf()
            _movies.value = UIState.Result(allMovies.toList())
        }

        if (currentPage <= totalPages) {
            viewModelScope.launch {
                _movies.value = if (currentPage == 1)
                    UIState.InProgress // show central loader only first time
                else UIState.IDLE // change the state because flows collector are not triggered if you pass same value
                try {
                    val response = repository.getMovies(currentPage = currentPage)
                    if (response.totalResults == 0)
                        _movies.value =
                            UIState.Error(EmptyResponseDataException(if (currentPage == 1) "empty" else "end"))
                    else {
                        currentPage = if (response.totalPages > currentPage) {
                            totalPages = response.totalPages
                            currentPage + 1
                        } else {
                            response.totalPages + 1
                        }
                        response.moviesNetwork.forEach { movie ->
                            val uiMovie = movie.toHomeMovie()
                            if (!allMovies.contains(uiMovie)) allMovies.add(uiMovie)
                        }
                        _movies.value = UIState.Result(allMovies)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _movies.value = UIState.Error(ex)
                }
            }
            return true
        } else {
            return false
        }
    }
}