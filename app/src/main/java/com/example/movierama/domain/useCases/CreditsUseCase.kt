package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class CreditsUseCase@Inject constructor(
    private val repository: MoviesRepository
) {

    private val _creditsState = MutableStateFlow(CreditsDetails())
    val creditsState: StateFlow<CreditsDetails> = _creditsState.asStateFlow()

    var movieId: Long = 0

    suspend fun getCredits() {
        val creditsDetails = try {
            val creditsResponse = repository.getMovieCredits(movieId)
            CreditsDetails(
                director = creditsResponse.getDirector(),
                cast = creditsResponse.getCast()
            )
        } catch (ex: Exception) {
            CreditsDetails()
        }
        _creditsState.update {
            it.copy(
                director = creditsDetails.director,
                cast = creditsDetails.cast
            )
        }
    }
}


data class CreditsDetails(
    val director: String = "",
    val cast: String = ""
)

