package com.example.movierama.domain.useCases

import com.example.movierama.domain.dispatchers.FakeDispatcherImpl
import com.example.movierama.features.favourites.domain.FavouriteUseCase
import com.example.movierama.features.favourites.data.StoredFavouriteMovie
import com.example.movierama.core.domain.storage.localdb.FavouriteMovieDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class FavouriteUseCaseTest {

    private lateinit var favouriteMovieDao: FavouriteMovieDao
    private lateinit var useCase: FavouriteUseCase

    val fakeStoredMovie: StoredFavouriteMovie
        get() = StoredFavouriteMovie(
            id = 1L,
            title = "",
            poster = "",
            rating = 0F,
            releaseDate = ""
        )

    @Before
    fun setUp() {
        favouriteMovieDao = mock()
        useCase = FavouriteUseCase(favouriteMovieDao, FakeDispatcherImpl())
    }

    @Test
    fun `test on favourite change when movie is not at favourites`() = runBlocking {
        // Given
        `when`(favouriteMovieDao.isMovieFavorite(fakeStoredMovie.id)).thenReturn(false)

        // When
        useCase.onFavouriteChanged(fakeStoredMovie)

        // Then
        // Verify that insertFavoriteMovie is called with the movie
        verify(favouriteMovieDao).insertFavoriteMovie(fakeStoredMovie)
    }

    @Test
    fun `test on favourite change when movie is already at favourites`() = runBlocking {
        // Given
        `when`(favouriteMovieDao.isMovieFavorite(fakeStoredMovie.id)).thenReturn(true)

        // When
        useCase.onFavouriteChanged(fakeStoredMovie)

        // Then
        // Verify that insertFavoriteMovie is called with the movie
        verify(favouriteMovieDao).deleteFavoriteMovie(fakeStoredMovie)
    }

    @Test
    fun `test fetchFavouriteMovies returns a list of favorite movies`() = runBlocking {
        // Given
        val movies = listOf(
            StoredFavouriteMovie(1, "Movie 1", "poster 1", 1F, "date 1"),
            StoredFavouriteMovie(2, "Movie 2", "poster 2", 2F, "date 2"),
            StoredFavouriteMovie(3, "Movie 3", "poster 3", 3F, "date 3"),
        )
        `when`(favouriteMovieDao.getAllFavoriteMovies()).thenReturn(flowOf(movies))

        // When
        val result = useCase.fetchFavouriteMovies().first()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].title).isEqualTo("Movie 1")
    }


}
