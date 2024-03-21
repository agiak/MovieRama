package com.example.movierama.domain.movies

import com.example.movierama.core.domain.movies.MoviesRepository
import com.example.movierama.core.domain.movies.MoviesRepositoryImpl
import com.example.movierama.domain.dispatchers.FakeDispatcherImpl
import com.example.movierama.features.details.data.Cast
import com.example.movierama.features.details.data.CreditsResponse
import com.example.movierama.features.details.data.Crew
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.features.details.data.AuthorDetails
import com.example.movierama.features.details.data.ReviewNetwork
import com.example.movierama.features.details.data.ReviewsResponse
import com.example.movierama.features.details.data.SimilarMovieNetwork
import com.example.movierama.features.details.data.SimilarResponse
import com.example.movierama.core.domain.movies.MoviesService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class MoviesRepositoryTest {

    private val service: MoviesService = mock()

    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        repository = MoviesRepositoryImpl(
            dispatchers = FakeDispatcherImpl(),
            service = service
        )
    }

    @Test
    fun `test get popular movies with page 1 and empty list`() = runBlocking {
        // Given
        val page = 1
        `when`(service.getPopularMovies(page = page)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 1,
                totalResults = 2
            )
        )

        // When
        val result = repository.getPopularMovies(currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
    }

    @Test
    fun `test get top rated movies with page 1`() = runBlocking {
        // Given
        val page = 1
        `when`(service.getTopRatedMovies(page = page)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 100,
                totalResults = 2
            )
        )

        // When
        val result = repository.getTopRatedMovies(currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
        assertThat(result.totalPages).isEqualTo(100)
    }

    @Test
    fun `test get upcoming movies with page 1`() = runBlocking {
        // Given
        val page = 1
        `when`(service.getUpcomingMovies(page = page)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 100,
                totalResults = 2
            )
        )

        // When
        val result = repository.getUpcomingMovies(currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
        assertThat(result.totalPages).isEqualTo(100)
    }

    @Test
    fun `test get now playing movies with page 1`() = runBlocking {
        // Given
        val page = 1
        `when`(service.getNowPlayingMovies(page = page)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 100,
                totalResults = 2
            )
        )

        // When
        val result = repository.getNowPlayingMovies(currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
        assertThat(result.totalPages).isEqualTo(100)
    }

    @Test
    fun `test get popular movies with page 1 and total pages 100`() = runBlocking {
        // Given
        val page = 1
        `when`(service.getPopularMovies(page = page)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 100,
                totalResults = 2
            )
        )

        // When
        val result = repository.getPopularMovies(currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
        assertThat(result.totalPages).isEqualTo(100)
    }


    @Test
    fun `test searchMovies with page 1 and movieName`() = runBlocking {
        // Given
        val page = 1
        val movieName = "lord of the"
        `when`(service.searchMovies(page = page, movieName = movieName)).thenReturn(
            MoviesResponse(
                page = 1,
                moviesNetwork = emptyList(),
                totalPages = 1,
                totalResults = 2
            )
        )

        // When
        val result = repository.searchMovies(page = page, movieName = movieName, year = null)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.moviesNetwork).isEmpty()
        assertThat(result.totalPages).isEqualTo(1)
    }

    @Test
    fun `test get reviews with page 1`() = runBlocking {
        // Given
        val movieId = 1L
        val page = 1
        val dummyReviewsList = listOf(
            ReviewNetwork(
                "1",
                "Author 1",
                AuthorDetails("avatar_path_1", "Name 1", 4.5, "username_1"),
                "Review 1",
                "2021-07-01",
                "2021-07-02",
                "https://example.com/review1"
            ),
            ReviewNetwork(
                "2",
                "Author 2",
                AuthorDetails("avatar_path_2", "Name 2", 3.5, "username_2"),
                "Review 2",
                "2021-07-03",
                "2021-07-04",
                "https://example.com/review2"
            )
        )
        `when`(service.getReviews(movieId = movieId, page = page)).thenReturn(
            ReviewsResponse(dummyReviewsList, page = 1, totalPages = 1, totalResults = 2)
        )

        // When
        val result = repository.getReviews(movieId = movieId, currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.reviewNetworks.size).isEqualTo(dummyReviewsList.size)
        assertThat(result.reviewNetworks[0].id).isEqualTo("1")
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalResults).isEqualTo(dummyReviewsList.size)
    }

    @Test
    fun `test get similarMovies with page 1`() = runBlocking {
        // Given
        val movieId = 1L
        val page = 1
        val dummySimilarMoviesList = listOf(
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
        )
        `when`(service.getSimilarMovies(movieId = movieId, page = page)).thenReturn(
            SimilarResponse(
                similarMovieNetworks = dummySimilarMoviesList,
                page = 1,
                totalPages = 1,
                totalResults = 2
            )
        )

        // When
        val result = repository.getSimilarMovies(movieId = movieId, currentPage = page)

        // Then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.similarMovieNetworks.size).isEqualTo(dummySimilarMoviesList.size)
        assertThat(result.similarMovieNetworks[0].id).isEqualTo(1L)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalResults).isEqualTo(dummySimilarMoviesList.size)
    }

    @Test
    fun `test get movie credits with movie id 1`() = runBlocking {
        // Given
        val movieId = 1L
        `when`(service.getMovieCredits(movieId = movieId)).thenReturn(
            CreditsResponse(
                id = movieId,
                castList = getFakeCastList(),
                crewList = getFakeCrewList()
            )
        )

        // When
        val result = repository.getMovieCredits(movieId = movieId)

        // Then
        assertThat(result.castList.size).isEqualTo(2)
        assertThat(result.crewList.size).isEqualTo(2)
    }

    private fun getFakeCastList() =
        listOf(
            Cast(
                false,
                123,
                "Character 1",
                "credit_id_1",
                0,
                456,
                "Acting",
                "Name 1",
                1,
                "Original Name 1",
                7.8,
                "/path1.jpg"
            ), Cast(
                true,
                234,
                "Character 2",
                "credit_id_2",
                1,
                567,
                "Acting",
                "Name 2",
                2,
                "Original Name 2",
                6.5,
                "/path2.jpg"
            )
        )

    private fun getFakeCrewList() =
        listOf(
            Crew(
                false,
                "credit_id_1",
                "Directing",
                0,
                123,
                "Job 1",
                "Known Department 1",
                "Name 1",
                "Original Name 1",
                7.8,
                "/path1.jpg"
            ),
            Crew(
                true,
                "credit_id_2",
                "Directing",
                1,
                234,
                "Job 2",
                "Known Department 2",
                "Name 2",
                "Original Name 2",
                6.5,
                "/path2.jpg"
            )
        )


}
