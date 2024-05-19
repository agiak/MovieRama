package com.example.movierama.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.movies.toHomeMovie
import com.example.movierama.features.home.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : ViewModel() {

    private val popularData =
        repository.fetchPopularMovies().cachedIn(viewModelScope)
            .map { it.map { movieNetwork -> movieNetwork.toHomeMovie() } }

    private val upcomingData =
        repository.fetchUpcomingMovies().cachedIn(viewModelScope)
            .map { it.map { movieNetwork -> movieNetwork.toHomeMovie() } }

    private val nowPlayingData =
        repository.fetchNowPlayingMovies().cachedIn(viewModelScope)
            .map { it.map { movieNetwork -> movieNetwork.toHomeMovie() } }

    private val topRatedData =
        repository.fetchTopRatedMovies().cachedIn(viewModelScope)
            .map { it.map { movieNetwork -> movieNetwork.toHomeMovie() } }

    val moviesDataMap = hashMapOf(
        MoviesType.POPULAR to popularData,
        MoviesType.UPCOMING to upcomingData,
        MoviesType.NOW_PLAYING to nowPlayingData,
        MoviesType.TOP_RATED to topRatedData,
    )
}
