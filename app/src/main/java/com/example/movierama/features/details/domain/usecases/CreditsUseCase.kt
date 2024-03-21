package com.example.movierama.features.details.domain.usecases

import com.example.movierama.features.details.domain.DetailsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Use case class responsible for managing the credits data for a movie.
 * It retrieves the credits information from the [DetailsRepository] and updates the [creditsState].
 *
 * @property repository The repository used to fetch movie credits data.
 * @property _creditsState The mutable state flow for the credits details.
 * @property creditsState The immutable state flow representing the credits details.
 * @property movieId The ID of the movie for which the credits are fetched.
 */
@ViewModelScoped
class CreditsUseCase @Inject constructor(
    private val repository: DetailsRepository
) {

    private val _creditsState = MutableStateFlow(CreditsDetails())
    val creditsState: StateFlow<CreditsDetails> = _creditsState.asStateFlow()

    var movieId: Long = 0

    /**
     * Retrieves the credits details for the specified movie.
     * If successful, updates the [_creditsState] with the fetched details.
     * If an exception occurs, updates the [_creditsState] with empty details.
     */
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

/**
 * Data class representing the credits details for a movie.
 *
 * @property director The name of the director.
 * @property cast The cast information.
 */
data class CreditsDetails(
    val director: String = "",
    val cast: String = ""
)

