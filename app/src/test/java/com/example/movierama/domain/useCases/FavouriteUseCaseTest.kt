package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class FavouriteUseCaseTest {

    @Test
    fun `onFavouriteChanged should call repository's onFavouriteChange`() {
        // Arrange
        val movieId = 123L
        val repository = mock<MoviesRepository>()
        val favouriteUseCase = FavouriteUseCase(repository)

        // Act
        favouriteUseCase.onFavouriteChanged(movieId)

        // Assertion
        verify(repository).onFavouriteChange(movieId)
    }
}