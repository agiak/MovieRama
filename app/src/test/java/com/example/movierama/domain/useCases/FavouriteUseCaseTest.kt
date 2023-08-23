package com.example.movierama.domain.useCases

import com.example.movierama.domain.movies.MoviesRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class FavouriteUseCaseTest {

    @Test
    fun `onFavouriteChanged should call repository's onFavouriteChange`() {
        // Given
        val movieId = 123L
        val repository = mock<MoviesRepository>()
        val favouriteUseCase = FavouriteUseCase(repository)

        // When
        favouriteUseCase.onFavouriteChanged(movieId)

        // Then
        verify(repository).onFavouriteChange(movieId)
    }

    @Test
    fun `is movie saved as favourite`() {
        // Given
        val movieId = 123L
        val repository = mock<MoviesRepository>()
        val favouriteUseCase = FavouriteUseCase(repository)
        Mockito.`when`(repository.isMovieFavourite(movieId)).thenReturn(true)

        // When
        val result = favouriteUseCase.isMovieFavourite(movieId)

        // Then
        assertThat(result).isTrue()
    }
}
