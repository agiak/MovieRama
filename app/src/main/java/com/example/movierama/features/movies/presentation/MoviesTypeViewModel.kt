package com.example.movierama.features.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.flatMap
import androidx.paging.map
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.movies.toHomeMovie
import com.example.movierama.core.data.movies.toStoredFavouriteMovie
import com.example.movierama.core.data.movies.toUiMovieList
import com.example.movierama.features.favourites.domain.FavouriteRepository
import com.example.movierama.features.movies.domain.MoviesTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesTypeViewModel @Inject constructor(
    private val moviesRepository: FavouriteRepository,
    private val repository: MoviesTypeRepository,
) : ViewModel() {

    var movieType: MoviesType = MoviesType.POPULAR

    val movies by lazy {
        repository.getMovies(movieType).cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { movieNetwork ->
                movieNetwork.toHomeMovie().apply { setIsFavouriteToMovies() }
            }
        }
    }

    fun onFavouriteChanged(movie: Movie) {
        viewModelScope.launch {
            moviesRepository.onFavouriteStatusChanged(movie.toStoredFavouriteMovie())
        }
    }

    private suspend fun Movie.setIsFavouriteToMovies() {
        isFavourite = moviesRepository.isMovieFavourite(id)
    }
}
