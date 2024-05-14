package com.example.movierama.domain.useCases

import com.example.movierama.domain.error_handling.FakeErrorHandler
import com.example.movierama.core.domain.movies.MoviesRepository
import com.example.movierama.features.favourites.domain.FavouriteUseCase
import com.example.movierama.features.details.domain.usecases.MovieDetailsUseCase
import com.example.movierama.features.details.data.Genre
import com.example.movierama.features.details.data.MovieDetailsResponse
import com.example.movierama.features.details.data.ProductionCompany
import com.example.movierama.features.details.data.ProductionCountry
import com.example.movierama.features.details.data.SpokenLanguage
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
    private lateinit var favouriteUseCase: FavouriteUseCase

    @Before
    fun setup() {
        repository = mock()
        favouriteUseCase = mock()
        movieDetailsUseCase = MovieDetailsUseCase(
            favouriteUseCase = favouriteUseCase,
            repository = repository,
            errorHandler = FakeErrorHandler()
        )
    }

    @Test
    fun `getMovieDetails should update movieDetailsState with the correct values`() = runBlocking {
        // Given
        val movieId = 123L
        val movieResponse = MovieDetailsResponse(
            id = movieId,
            adult = false,
            backdropPath = "backdrop_path",
            belongsToCollection = Any(),
            budget = 10000000,
            genres = listOf(Genre(id = 1, name = "Action"), Genre(id = 2, name = "Adventure")),
            homepage = "https://www.example.com/movie",
            imdbId = "tt1234567",
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Movie overview",
            popularity = 7.8,
            posterPath = "poster_path",
            productionCompanies = listOf(
                ProductionCompany(
                    id = 1,
                    logoPath = "logo_path",
                    name = "Company 1",
                    originCountry = "US"
                ),
                ProductionCompany(
                    id = 2,
                    logoPath = "logo_path",
                    name = "Company 2",
                    originCountry = "UK"
                )
            ),
            productionCountries = listOf(
                ProductionCountry(iso31661 = "US", name = "United States"),
                ProductionCountry(iso31661 = "UK", name = "United Kingdom")
            ),
            releaseDate = "2023-06-25",
            revenue = 50000000,
            runtime = 120,
            spokenLanguages = listOf(
                SpokenLanguage(englishName = "English", iso6391 = "en", name = "English")
            ),
            status = "Released",
            tagline = "Movie tagline",
            title = "Title",
            video = false,
            rating = 8.0F,
            votes = 1000
        )

        `when`(repository.getMovie(movieId)).thenReturn(movieResponse)

        // When
        movieDetailsUseCase.movieId = movieId
        movieDetailsUseCase.execute()
        val movieDetailsState = movieDetailsUseCase.movieDetailsState.first()

        // Then
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
            // Given
            val movieId = 123L
            `when`(repository.getMovie(movieId)).thenThrow(RuntimeException("Error fetching movie details"))

            // When
            movieDetailsUseCase.movieId = movieId
            movieDetailsUseCase.execute()
            val movieDetailsState = movieDetailsUseCase.movieDetailsState.first()

            // Then
            assertThat(movieDetailsState.movieDetails).isNull()
            assertThat(movieDetailsState.isLoading).isFalse()
            assertThat(movieDetailsState.errorMessage).isEqualTo("Error fetching movie details")
        }
}
