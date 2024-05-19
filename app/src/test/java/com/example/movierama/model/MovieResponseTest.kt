package com.example.movierama.model

import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.data.movies.toHomeMovie
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MovieResponseTest {

    @Test
    fun `test converting MovieNetwork object to Movie one`() {
        // Given
        val givenData = MovieNetwork(
            adult = false,
            backdropPath = "http://image.tmdb.org/t/p/w500",
            genreIds = listOf(80, 18, 9648),
            id = 13579L,
            originalLanguage = "en",
            originalTitle = "Original Title 5",
            overview = "Movie 5 overview",
            popularity = 5.9,
            posterPath = "/path/to/poster5.jpg",
            releaseDate = "2023-06-25",
            title = "Movie Title 5",
            video = false,
            voteAverage = 6.0f,
            voteCount = 1200
        )

        // When
        val expectedResult = Movie(
            id = 13579L,
            title = "Movie Title 5",
            rating = 3.0f,
            releaseDate = "25 June 23",
            poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
            isFavourite = false
        )

        // Then
        assertThat(expectedResult).isEqualTo(givenData.toHomeMovie())
    }

    @Test
    fun `test converting list of MovieNetwork objects to a list of Movie ones`() {
        val givenNetworkMovies = listOf(
            MovieNetwork(
                adult = false,
                backdropPath = "http://image.tmdb.org/t/p/w500",
                genreIds = listOf(80, 18, 9648),
                id = 13579L,
                originalLanguage = "en",
                originalTitle = "Original Title 5",
                overview = "Movie 5 overview",
                popularity = 5.9,
                posterPath = "/path/to/poster5.jpg",
                releaseDate = "2023-06-25",
                title = "Movie Title 5",
                video = false,
                voteAverage = 6.0f,
                voteCount = 1200
            ), MovieNetwork(
                adult = false,
                backdropPath = "/path/to/backdrop5.jpg",
                genreIds = listOf(80, 18, 9648),
                id = 13579L,
                originalLanguage = "en",
                originalTitle = "Original Title 5",
                overview = "Movie 5 overview",
                popularity = 5.9,
                posterPath = "/path/to/poster5.jpg",
                releaseDate = "2023-11-25",
                title = "Movie Title 7",
                video = false,
                voteAverage = 4f,
                voteCount = 1200
            )
        )

        val givenResponse = MoviesResponse(
            page = 1,
            moviesNetwork = givenNetworkMovies,
            totalPages = 1,
            totalResults = 2
        )

        // When
        val expectedResult = listOf(
            Movie(
                id = 13579L,
                title = "Movie Title 5",
                rating = 3.0f,
                releaseDate = "25 June 23",
                poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
                isFavourite = false
            ),
            Movie(
                id = 13579L,
                title = "Movie Title 7",
                rating = 2f,
                releaseDate = "25 November 23",
                poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
                isFavourite = false
            )
        )

        // Then
        assertThat(expectedResult).isEqualTo(givenResponse.getUiMovies())
    }

}
