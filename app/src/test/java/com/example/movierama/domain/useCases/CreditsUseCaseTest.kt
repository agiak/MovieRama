package com.example.movierama.domain.useCases

import com.example.movierama.data.network.credits.Cast
import com.example.movierama.data.network.credits.CreditsResponse
import com.example.movierama.data.network.credits.Crew
import com.example.movierama.domain.movies.MoviesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Unit tests for the [CreditsUseCase] class.
 */
class CreditsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var creditsUseCase: CreditsUseCase

    @Before
    fun setup() {
        repository = mock()
        creditsUseCase = CreditsUseCase(repository)
    }

    @Test
    fun `getCredits should update creditsState with the correct values`() = runBlocking {
        // Given
        val movieId = 123L
        val crew = listOf(
            Crew(
                adult = false,
                credit_id = "credit_id_3",
                department = "Department 3",
                gender = 0,
                id = 789,
                job = "Job 1",
                known_for_department = "Known For Department 1",
                name = "Name 3",
                original_name = "Original Name 3",
                popularity = 3.0,
                profile_path = "profile_path_3"
            ),
            Crew(
                adult = false,
                credit_id = "credit_id_4",
                department = "Directing",
                gender = 0,
                id = 101,
                job = "Job 2",
                known_for_department = "Known For Department 4",
                name = "Name 4",
                original_name = "Original Name 4",
                popularity = 4.0,
                profile_path = "profile_path_4"
            )
        )
        val cast = listOf(
            Cast(
                adult = false,
                cast_id = 1,
                character = "Character 1",
                credit_id = "credit_id_1",
                gender = 0,
                id = 123,
                known_for_department = "Acting",
                name = "Name 1",
                order = 1,
                original_name = "Original Name 1",
                popularity = 1.0,
                profile_path = "profile_path_1"
            ),
            Cast(
                adult = false,
                cast_id = 2,
                character = "Character 2",
                credit_id = "credit_id_2",
                gender = 0,
                id = 456,
                known_for_department = "Department 2",
                name = "Name 2",
                order = 2,
                original_name = "Original Name 2",
                popularity = 2.0,
                profile_path = "profile_path_2"
            )
        )

        // Mock repository response
        val creditsResponse = CreditsResponse(cast, crew, movieId.toInt())
        `when`(repository.getMovieCredits(movieId)).thenReturn(creditsResponse)

        // When
        creditsUseCase.movieId = movieId
        creditsUseCase.getCredits()
        val creditsState = creditsUseCase.creditsState.first()

        // Then
        assertThat(creditsState.director).isEqualTo("Name 4")
        assertThat(creditsState.cast).isEqualTo("Name 1")
    }

    @Test
    fun `getCredits should update creditsState with empty values when an exception occurs`() = runBlocking {
        // Given
        val movieId = 123L
        `when`(repository.getMovieCredits(movieId)).thenThrow(RuntimeException())

        // When
        creditsUseCase.movieId = movieId
        creditsUseCase.getCredits()
        val creditsState = creditsUseCase.creditsState.first()

        // Then
        assertThat(creditsState.director).isEmpty()
        assertThat(creditsState.cast).isEmpty()
    }
}
