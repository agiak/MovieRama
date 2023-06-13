package com.example.movierama.data

import com.example.movierama.data.network.movies.MovieNetwork
import com.example.movierama.data.network.movies.MoviesResponse
import com.google.common.truth.Truth
import org.junit.Test

class MovieResponseTest {

    @Test
    fun `test converting MovieNetwork object to Movie one`() {
        // Give data
        val givenData = MovieNetwork(
            adult = false,
            backdrop_path = "http://image.tmdb.org/t/p/w500",
            genre_ids = listOf(80, 18, 9648),
            id = 13579L,
            original_language = "en",
            original_title = "Original Title 5",
            overview = "Movie 5 overview",
            popularity = 5.9,
            poster_path = "/path/to/poster5.jpg",
            release_date = "2023-06-25",
            title = "Movie Title 5",
            video = false,
            vote_average = 6.0f,
            vote_count = 1200
        )

        // Expected result
        val expectedResult = Movie(
            id = 13579L,
            title = "Movie Title 5",
            rating = 3.0f,
            releaseDate = "25 June 23",
            poster = "http://image.tmdb.org/t/p/w500" + "/path/to/poster5.jpg",
            isFavourite = false
        )

        // Assertions
        Truth.assertThat(expectedResult).isEqualTo(givenData.toHomeMovie())
    }

    @Test
    fun `test converting list of MovieNetwork objects to a list of Movie ones`() {
        val givenNetworkMovies = listOf(
            MovieNetwork(
                adult = false,
                backdrop_path = "http://image.tmdb.org/t/p/w500",
                genre_ids = listOf(80, 18, 9648),
                id = 13579L,
                original_language = "en",
                original_title = "Original Title 5",
                overview = "Movie 5 overview",
                popularity = 5.9,
                poster_path = "/path/to/poster5.jpg",
                release_date = "2023-06-25",
                title = "Movie Title 5",
                video = false,
                vote_average = 6.0f,
                vote_count = 1200
            ), MovieNetwork(
                adult = false,
                backdrop_path = "/path/to/backdrop5.jpg",
                genre_ids = listOf(80, 18, 9648),
                id = 13579L,
                original_language = "en",
                original_title = "Original Title 5",
                overview = "Movie 5 overview",
                popularity = 5.9,
                poster_path = "/path/to/poster5.jpg",
                release_date = "2023-11-25",
                title = "Movie Title 7",
                video = false,
                vote_average = 4f,
                vote_count = 1200
            )
        )

        val givenResponse = MoviesResponse(
            page = 1,
            moviesNetwork = givenNetworkMovies,
            totalPages = 1,
            totalResults = 2
        )

        // Expected result
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

        // Assertions
        Truth.assertThat(expectedResult).isEqualTo(givenResponse.getUiMovies())
    }

}