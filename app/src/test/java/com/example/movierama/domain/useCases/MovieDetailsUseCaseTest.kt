package com.example.movierama.domain.useCases

import com.example.movierama.data.network.movies.Genre
import com.example.movierama.data.network.movies.MovieDetailsResponse
import com.example.movierama.data.network.movies.ProductionCompany
import com.example.movierama.data.network.movies.ProductionCountry
import com.example.movierama.data.network.movies.SpokenLanguage
import com.example.movierama.domain.movies.MoviesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class MovieDetailsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var movieDetailsUseCase: MovieDetailsUseCase

    @Before
    fun setup() {
        repository = mock()
        movieDetailsUseCase = MovieDetailsUseCase(repository)
    }

    @Test
    fun `getMovieDetails should update movieDetailsState with the correct values`() = runBlocking {
        // Given data
        val movieId = 123L
        val movieResponse = MovieDetailsResponse(
            id = movieId,
            adult = false,
            backdrop_path = "backdrop_path",
            belongs_to_collection = Any(),
            budget = 10000000,
            genres = listOf(Genre(id = 1, name = "Action"), Genre(id = 2, name = "Adventure")),
            homepage = "https://www.example.com/movie",
            imdb_id = "tt1234567",
            original_language = "en",
            original_title = "Original Title",
            overview = "Movie overview",
            popularity = 7.8,
            posterPath = "poster_path",
            production_companies = listOf(
                ProductionCompany(
                    id = 1,
                    logo_path = "logo_path",
                    name = "Company 1",
                    origin_country = "US"
                ),
                ProductionCompany(
                    id = 2,
                    logo_path = "logo_path",
                    name = "Company 2",
                    origin_country = "UK"
                )
            ),
            production_countries = listOf(
                ProductionCountry(iso_3166_1 = "US", name = "United States"),
                ProductionCountry(iso_3166_1 = "UK", name = "United Kingdom")
            ),
            releaseDate = "2023-06-25",
            revenue = 50000000,
            runtime = 120,
            spoken_languages = listOf(
                SpokenLanguage(english_name = "English", iso_639_1 = "en", name = "English")
            ),
            status = "Released",
            tagline = "Movie tagline",
            title = "Title",
            video = false,
            rating = 8.0F,
            votes = 1000
        )

        `when`(repository.getMovie(movieId)).thenReturn(movieResponse)

        // Expected data
        movieDetailsUseCase.movieId = movieId
        movieDetailsUseCase.getMovieDetails()
        val movieDetailsState = movieDetailsUseCase.movieDetailsState.first()

        // Assertions
        assertThat(movieDetailsState.movieDetails?.id).isEqualTo(movieId)
        assertThat(movieDetailsState.movieDetails?.title).isEqualTo("Title")
        assertThat(movieDetailsState.movieDetails?.type).isEqualTo("Action, Adventure")
        assertThat(movieDetailsState.movieDetails?.releaseDate).isEqualTo("25 June 23")
        assertThat(movieDetailsState.movieDetails?.rating).isEqualTo(4.0F)
        assertThat(movieDetailsState.movieDetails?.poster).isEqualTo("http://image.tmdb.org/t/p/w500poster_path")
        assertThat(movieDetailsState.movieDetails?.isFavourite).isFalse()
        assertThat(movieDetailsState.movieDetails?.description).isEqualTo("Movie overview")
        assertThat(movieDetailsState.isLoading).isFalse()
        assertThat(movieDetailsState.errorMessage).isEmpty()
    }

    @Test
    fun `getMovieDetails should update movieDetailsState with empty values when an exception occurs`() =
        runBlocking {
            // Given Data
            val movieId = 123L
            `when`(repository.getMovie(movieId)).thenThrow(RuntimeException("Error fetching movie details"))

            // Expected data
            movieDetailsUseCase.movieId = movieId
            movieDetailsUseCase.getMovieDetails()
            val movieDetailsState = movieDetailsUseCase.movieDetailsState.first()

            // Assertions
            assertThat(movieDetailsState.movieDetails).isNull()
            assertThat(movieDetailsState.isLoading).isFalse()
            assertThat(movieDetailsState.errorMessage).isEqualTo("Error fetching movie details")
        }
}
