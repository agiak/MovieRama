package com.example.movierama.domain.useCases

import com.example.movierama.domain.FakeMovieRepository
import com.example.movierama.model.MoviesType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FetchMovieUseCaseTest {

    private lateinit var useCase: FetchMoviesUseCase
    private val repository = FakeMovieRepository()

    @Before
    fun setup() {
        useCase = FetchMoviesUseCase(repository)
    }

    @Test
    fun `fetch movies when type is popular`() = runBlocking {
        // Given
        val movieType = MoviesType.POPULAR
        val page = 1

        // When
        val result = useCase.fetchMovies(movieType, page)

        // Then
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `fetch movies when type is now playing`() = runBlocking {
        // Given
        val movieType = MoviesType.NOW_PLAYING
        val page = 1

        // When
        val result = useCase.fetchMovies(movieType, page)

        // Then
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `fetch movies when type is top rated`() = runBlocking {
        // Given
        val movieType = MoviesType.TOP_RATED
        val page = 1

        // When
        val result = useCase.fetchMovies(movieType, page)

        // Then
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `fetch movies when type is upcoming`() = runBlocking {
        // Given
        val movieType = MoviesType.UPCOMING
        val page = 1

        // When
        val result = useCase.fetchMovies(movieType, page)

        // Then
        assertThat(result.page).isEqualTo(1)
    }

}
