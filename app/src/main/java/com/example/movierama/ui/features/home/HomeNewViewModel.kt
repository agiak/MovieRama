package com.example.movierama.ui.features.home

import androidx.lifecycle.ViewModel
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeNewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val fetchMoviesUseCase: FetchMoviesUseCase,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState
}

sealed class HomeState {
    object Loading: HomeState()
    data class PopularData(val movies: List<Movie> = emptyList(), val isLoading: Boolean = false): HomeState()
    data class NowPlayingData(val movies: List<Movie> = emptyList(), val isLoading: Boolean = false): HomeState()
    data class TopRatedData(val movies: List<Movie> = emptyList(), val isLoading: Boolean = false): HomeState()
    data class UpcomingData(val movies: List<Movie> = emptyList(), val isLoading: Boolean = false): HomeState()
}