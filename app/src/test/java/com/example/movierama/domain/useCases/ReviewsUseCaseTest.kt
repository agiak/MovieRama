package com.example.movierama.domain.useCases

import com.example.movierama.data.network.reviews.AuthorDetails
import com.example.movierama.data.network.reviews.ReviewNetwork
import com.example.movierama.data.network.reviews.ReviewsResponse
import com.example.movierama.domain.movies.MoviesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ReviewsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: ReviewsUseCase

    @Before
    fun setup() {
        repository = Mockito.mock()
        useCase = ReviewsUseCase(repository)
    }

    @Test
    fun `loadReviews should update reviewsState with successful response`() = runBlocking {
        // Given data
        val movieId = 123L
        val currentPage = 1
        val reviewsResponse = createMockReviewsResponse() // Create a mock reviews response
        `when`(repository.getReviews(movieId, currentPage)).thenReturn(reviewsResponse)

        // Expected
        useCase.movieId = movieId
        useCase.loadReviews()

        // Assertions
        val reviewsState = useCase.reviewsState.first()
        assertThat(reviewsState.reviews).isEqualTo(reviewsResponse.reviewNetworks.toReviewList())
        assertThat(reviewsState.isLoading).isFalse()
        assertThat(reviewsState.errorMessage).isEmpty()
    }

    @Test
    fun `loadReviews should update reviewsState with error response`() = runBlocking {
        // Arrange
        val movieId = 123L
        val currentPage = 1
        val errorMessage = "Error loading reviews"
        val exception = RuntimeException(errorMessage)
        `when`(repository.getReviews(movieId, currentPage)).thenThrow(exception)

        // Act
        useCase.movieId = movieId
        useCase.loadReviews()

        // Assert
        val reviewsState = useCase.reviewsState.first()
        assertThat(reviewsState.reviews).isEmpty()
        assertThat(reviewsState.isLoading).isFalse()
        assertThat(reviewsState.errorMessage).isEqualTo(errorMessage)
    }

    @Test
    fun `loadReviews should handle empty review list`() = runBlocking {
        // Arrange
        val movieId = 123L
        val currentPage = 1
        val reviewsResponse = ReviewsResponse(emptyList(), page = 1, totalPages = 1, totalResults = 0)
        `when`(repository.getReviews(movieId, currentPage)).thenReturn(reviewsResponse)

        // Act
        useCase.movieId = movieId
        useCase.loadReviews()

        // Assert
        val reviewsState = useCase.reviewsState.first()
        assertThat(reviewsState.reviews).isEmpty()
        assertThat(reviewsState.isLoading).isFalse()
        assertThat(reviewsState.errorMessage).isEmpty()
    }

    private fun createMockReviewsResponse(totalPages: Int = 1): ReviewsResponse {
        // Create a mock ReviewsResponse object for testing
        val reviewNetworks = listOf(
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
        return ReviewsResponse(reviewNetworks, page = 1, totalPages = totalPages, totalResults = reviewNetworks.size)
    }
}
