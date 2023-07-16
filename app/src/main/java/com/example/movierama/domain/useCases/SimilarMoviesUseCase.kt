package com.example.movierama.domain.useCases

import com.example.movierama.domain.error_hadling.ErrorHandler
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.model.remote.similar.SimilarMovie
import com.example.movierama.model.remote.similar.SimilarMovieNetwork
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class SimilarMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val errorHandler: ErrorHandler
) {
    // StateFlow to hold the similar movies state
    private val _similarMoviesState = MutableStateFlow(SimilarMoviesState())
    val similarMoviesState: StateFlow<SimilarMoviesState> = _similarMoviesState.asStateFlow()

    var movieId: Long = 0

    // Set to store all similar movies
    private val allSimilarMovies: MutableSet<SimilarMovie> = mutableSetOf()

    // Variables to manage pagination
    var currentSimilarMoviesPage = 1
    var totalSimilarMoviesPages = 1

    // Helper function to emit loading state
    private fun emitLoadingState() {
        _similarMoviesState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    // Function to load more similar movies by incrementing the page number
    suspend fun loadMore() {
        // Check if there are more pages to load and not already loading
        if (currentSimilarMoviesPage < totalSimilarMoviesPages && similarMoviesState.value.isLoading.not()) {
            currentSimilarMoviesPage++
            loadMovies()
        }
    }

    // Function to load similar movies for the given movieId
    suspend fun loadMovies() {
        emitLoadingState()
        _similarMoviesState.update {
            val similarMoviesState = try {
                // Fetch similar movies from the repository
                val similarResponse =
                    repository.getSimilarMovies(
                        movieId = movieId,
                        currentPage = currentSimilarMoviesPage
                    )
                totalSimilarMoviesPages = similarResponse.totalPages

                // Add the fetched similar movies to the set
                allSimilarMovies.addAll(similarResponse.similarMovieNetworks.toSimilarMovieList())

                SimilarMoviesState(
                    similarMovies = allSimilarMovies.toList(),
                    isLoading = false,
                    errorMessage = ""
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                SimilarMoviesState(
                    similarMovies = emptyList(),
                    isLoading = false,
                    errorMessage = errorHandler.getErrorMessage(ex)
                )
            }
            // Update the similarMoviesState with the fetched movies and error state
            it.copy(
                similarMovies = similarMoviesState.similarMovies,
                isLoading = similarMoviesState.isLoading,
                errorMessage = similarMoviesState.errorMessage
            )
        }
    }
}

// Data class representing the state of similar movies
data class SimilarMoviesState(
    val similarMovies: List<SimilarMovie> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    // Helper function to check if there is an error message
    fun hasError() = errorMessage.isNotEmpty()
}

// Extension function to convert a list of SimilarMovieNetwork to SimilarMovie list
fun List<SimilarMovieNetwork>.toSimilarMovieList(): List<SimilarMovie> =
    ArrayList<SimilarMovie>().apply {
        this@toSimilarMovieList.forEach {
            add(it.toUiSimilarMovie())
        }
    }
