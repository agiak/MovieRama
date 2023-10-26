package com.example.movierama.model

import com.example.movierama.model.storage.StoredFavouriteMovie
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MovieModelMapperTest {

    @Test
    fun `convert movie model to StoredFavouriteMovie model`() {
        // Given
        val movie = Movie(
            id = 13579L,
            title = "Movie Title 5",
            rating = 3.0f,
            releaseDate = "25 June 23",
            poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
            isFavourite = false
        )

        // When
        val result = movie.toStoredFavouriteMovie()

        // Then
        assertThat(result.id).isEqualTo(13579L)
        assertThat(result.title).isEqualTo("Movie Title 5")
        assertThat(result.poster).isEqualTo("http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg")
        assertThat(result.rating).isEqualTo(3.0f)
        assertThat(result.releaseDate).isEqualTo("25 June 23")
    }

    @Test
    fun `convert movie details model to StoredFavouriteMovie model`() {
        // Given
        val movieDetails = MovieDetails(
            id = 13579L,
            title = "Movie Title 5",
            rating = 3.0f,
            releaseDate = "25 June 23",
            poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
            isFavourite = false,
            description = "description",
            type = "type"
        )

        // When
        val result = movieDetails.toStoredFavouriteMovie()

        // Then
        assertThat(result.id).isEqualTo(13579L)
        assertThat(result.title).isEqualTo("Movie Title 5")
        assertThat(result.poster).isEqualTo("http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg")
        assertThat(result.rating).isEqualTo(3.0f)
        assertThat(result.releaseDate).isEqualTo("25 June 23")
    }

    @Test
    fun `convert StoredFavouriteMovie to movie model`() {
        // Given
        val storedFavouriteMovie = StoredFavouriteMovie(
            id = 13579L,
            title = "Movie Title 5",
            rating = 3.0f,
            releaseDate = "25 June 23",
            poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg"
        )

        // When
        val result = storedFavouriteMovie.toUiMovie()

        // Then
        assertThat(result.id).isEqualTo(13579L)
        assertThat(result.title).isEqualTo("Movie Title 5")
        assertThat(result.poster).isEqualTo("http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg")
        assertThat(result.rating).isEqualTo(3.0f)
        assertThat(result.releaseDate).isEqualTo("25 June 23")
        assertThat(result.isFavourite).isTrue()
    }

    @Test
    fun `convert StoredFavouriteMovie to ui movie model list`() {
        // Given
        val favouriteMovies = listOf(
            StoredFavouriteMovie(
                id = 1L,
                title = "Movie Title 5",
                rating = 3.0f,
                releaseDate = "25 June 23",
                poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster1.jpg"
            ),
            StoredFavouriteMovie(
                id = 2L,
                title = "Movie Title 5",
                rating = 3.0f,
                releaseDate = "25 June 23",
                poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster2.jpg"
            )
        )

        // When
        val result = favouriteMovies.toUiMovieList()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[1].id).isEqualTo(2L)
    }

    @Test
    fun `convert StoredFavouriteMovie to ui movie model list when list is empty`() {
        // Given
        val favouriteMovies = emptyList<StoredFavouriteMovie>()

        // When
        val result = favouriteMovies.toUiMovieList()

        // Then
        assertThat(result).isEmpty()
    }
}
