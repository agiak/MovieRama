package com.example.movierama.domain.useCases

import com.example.movierama.data.MovieDetails
import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class MovieDetailsUseCase@Inject constructor(
    private val repository: MoviesRepository
) {

    private val _movieDetailsState = MutableStateFlow(MovieDetailsState())
    val movieDetailsState: StateFlow<MovieDetailsState> = _movieDetailsState.asStateFlow()

    var movieId: Long = 0

    private fun emitLoadState() {
        _movieDetailsState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    suspend fun getMovieDetails() {
        emitLoadState()
        val movieDetailsState = try {
            val movieResponse =
                repository.getMovie(movieId = movieId)
            MovieDetailsState(
                movieDetails = movieResponse.toMovieDetails().also { movieDetails ->
                    movieDetails.setIsFavouriteToMovieDetails()
                },
                isLoading = false,
                errorMessage = ""
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            MovieDetailsState(
                movieDetails = null,
                isLoading = false,
                errorMessage = ex.message.toString()
            )
        }
        _movieDetailsState.update {
            it.copy(
                movieDetails = movieDetailsState.movieDetails,
                isLoading = movieDetailsState.isLoading,
                errorMessage = movieDetailsState.errorMessage
            )
        }
    }

    private fun MovieDetails.setIsFavouriteToMovieDetails() {
        isFavourite = repository.isMovieFavourite(id)
    }
}

data class MovieDetailsState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}

