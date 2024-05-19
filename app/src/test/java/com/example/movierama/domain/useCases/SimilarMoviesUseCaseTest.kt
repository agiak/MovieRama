package com.example.movierama.domain.useCases

import com.example.movierama.core.domain.movies.MoviesRepository
import com.example.movierama.domain.error_handling.FakeErrorHandler
import com.example.movierama.features.details.data.SimilarMovieNetwork
import com.example.movierama.features.details.data.SimilarResponse
import com.example.movierama.features.details.domain.usecases.SimilarMoviesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

class SimilarMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var similarMoviesUseCase: SimilarMoviesUseCase

    @Before
    fun setup() {
        repository = mock()
        similarMoviesUseCase = SimilarMoviesUseCase(repository, errorHandler = FakeErrorHandler())
    }

    @Test
    fun `loadMore should increment currentSimilarMoviesPage and load more movies`() = runBlocking {
        // Given
        similarMoviesUseCase.movieId = 123L
        val currentPage = 1
        val totalPages = 3
        val similarMoviesResponse = SimilarResponse(
            page = currentPage,
            similarMovieNetworks = emptyList(),
            totalPages = totalPages,
            totalResults = 0
        )
        `when`(repository.getSimilarMovies(similarMoviesUseCase.movieId, currentPage)).thenReturn(
            similarMoviesResponse
        )

        // When
        similarMoviesUseCase.loadMore()

        // Then
        assertThat(similarMoviesUseCase.currentSimilarMoviesPage).isEqualTo(2)
        verify(repository).getSimilarMovies(similarMoviesUseCase.movieId, 2)
    }

    @Test
    fun `loadMore should not make a call when currentSimilarMoviesPage is equal to totalSimilarMoviesPages`(): Unit =
        runBlocking {
            // Given
            similarMoviesUseCase.movieId = 123L
            similarMoviesUseCase.currentSimilarMoviesPage = 3
            similarMoviesUseCase.totalSimilarMoviesPages = 3

            // When
            similarMoviesUseCase.loadMore()

            // Then
            verify(repository)
        }

    @Test
    fun `loadMore should not make concurrent calls when isLoading is true`() = runBlocking {
        // Given
        similarMoviesUseCase.movieId = 123L
        similarMoviesUseCase.currentSimilarMoviesPage = 1
        similarMoviesUseCase.totalSimilarMoviesPages = 3
        similarMoviesUseCase.loadMovies() // Start initial load

        // When
        similarMoviesUseCase.loadMore()

        // Then
        verify(repository).getSimilarMovies(similarMoviesUseCase.movieId, 1)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `loadMovies should update similarMoviesState with an empty list when response is empty`() =
        runBlocking {
            // Given
            similarMoviesUseCase.movieId = 123L
            val currentPage = 1
            val totalPages = 1
            val similarMoviesResponse = SimilarResponse(
                page = currentPage,
                similarMovieNetworks = emptyList(),
                totalPages = totalPages,
                totalResults = 0
            )
            `when`(
                repository.getSimilarMovies(
                    similarMoviesUseCase.movieId,
                    currentPage
                )
            ).thenReturn(similarMoviesResponse)

            // When
            similarMoviesUseCase.loadMovies()
            val similarMoviesState = similarMoviesUseCase.similarMoviesState.first()

            // Then
            assertThat(similarMoviesState.similarMovies).isEmpty()
            assertThat(similarMoviesState.isLoading).isFalse()
            assertThat(similarMoviesState.errorMessage).isEmpty()
        }

    @Test
    fun `loadMovies should update similarMoviesState with an error message when an exception occurs`() =
        runBlocking {
            // Given
            similarMoviesUseCase.movieId = 123L
            val currentPage = 1
            `when`(
                repository.getSimilarMovies(
                    similarMoviesUseCase.movieId,
                    currentPage
                )
            ).thenThrow(RuntimeException("API error"))

            // When
            similarMoviesUseCase.loadMovies()
            val similarMoviesState = similarMoviesUseCase.similarMoviesState.first()

            // Then
            assertThat(similarMoviesState.similarMovies).isEmpty()
            assertThat(similarMoviesState.isLoading).isFalse()
            assertThat(similarMoviesState.errorMessage).isEqualTo("API error")
        }

    @Test
    fun `loadMovies should update similarMoviesState with the retrieved movies when the response is successful`() =
        runBlocking {
            // Given
            similarMoviesUseCase.movieId = 123L
            val currentPage = 1
            val totalPages = 3
            val similarMoviesResponse = SimilarResponse(
                page = currentPage,
                similarMovieNetworks = listOf(
                    SimilarMovieNetwork(
                        id = 1L,
                        posterPath = "/poster1.jpg",
                        adult = false,
                        backdropPath = "",
                        genreIds = listOf(),
                        originalLanguage = "",
                        originalTitle = "",
                        overview = "",
                        popularity = 0.0,
                        releaseDate = "",
                        title = "",
                        video = false,
                        voteAverage = 0.0,
                        voteCount = 0
                    ),
                    SimilarMovieNetwork(
                        id = 2L,
                        posterPath = "/poster2.jpg",
                        adult = false,
                        backdropPath = "",
                        genreIds = listOf(),
                        originalLanguage = "",
                        originalTitle = "",
                        overview = "",
                        popularity = 0.0,
                        releaseDate = "",
                        title = "",
                        video = false,
                        voteAverage = 0.0,
                        voteCount = 0
                    )
                ),
                totalPages = totalPages,
                totalResults = 2
            )
            `when`(
                repository.getSimilarMovies(
                    similarMoviesUseCase.movieId,
                    currentPage
                )
            ).thenReturn(similarMoviesResponse)

            // When
            similarMoviesUseCase.loadMovies()
            val similarMoviesState = similarMoviesUseCase.similarMoviesState.first()

            // Then
            assertThat(similarMoviesState.similarMovies).hasSize(2)
            assertThat(similarMoviesState.isLoading).isFalse()
            assertThat(similarMoviesState.errorMessage).isEmpty()
        }
}
