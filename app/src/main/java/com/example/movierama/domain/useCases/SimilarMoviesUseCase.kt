package com.example.movierama.domain.useCases

import com.example.movierama.data.network.similar.SimilarMovie
import com.example.movierama.data.network.similar.SimilarMovieNetwork
import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class SimilarMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {

    private val _similarMoviesState = MutableStateFlow(SimilarMoviesState())
    val similarMoviesState: StateFlow<SimilarMoviesState> = _similarMoviesState.asStateFlow()

    var movieId: Long = 0

    private val allSimilarMovies: MutableSet<SimilarMovie> = mutableSetOf()
    private var currentSimilarMoviesPage = 1
    private var totalSimilarMoviesPages = 1

    private fun emitLoadingState() {
        _similarMoviesState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    suspend fun loadMore() {
        if (currentSimilarMoviesPage < totalSimilarMoviesPages && similarMoviesState.value.isLoading.not()) {
            currentSimilarMoviesPage++
            loadMovies()
        }
    }

    suspend fun loadMovies() {
        emitLoadingState()
        _similarMoviesState.update {
            val similarMoviesState = try {
                val similarResponse =
                    repository.getSimilarMovies(
                        movieId = movieId,
                        currentPage = currentSimilarMoviesPage
                    )
                totalSimilarMoviesPages = similarResponse.totalPages
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
                    errorMessage = ex.message.toString()
                )
            }
            it.copy(
                similarMovies = similarMoviesState.similarMovies,
                isLoading = similarMoviesState.isLoading,
                errorMessage = similarMoviesState.errorMessage
            )
        }
    }
}

data class SimilarMoviesState(
    val similarMovies: List<SimilarMovie> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}

fun List<SimilarMovieNetwork>.toSimilarMovieList(): List<SimilarMovie> =
    ArrayList<SimilarMovie>().apply {
        this@toSimilarMovieList.forEach {
            add(it.toUiSimilarMovie())
        }
    }
