package com.example.movierama.domain.useCases

import com.example.movierama.model.remote.reviews.Review
import com.example.movierama.model.remote.reviews.ReviewNetwork
import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class ReviewsUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    // MutableStateFlow to hold the state of reviews
    private val _reviewsState = MutableStateFlow(ReviewsState())
    val reviewsState: StateFlow<ReviewsState> = _reviewsState.asStateFlow()

    private val allReviews: MutableSet<Review> = mutableSetOf() // Set to store all reviews
    var currentReviewsPage = 1 // Current page of reviews
    var totalReviewsPages = 1 // Total number of review pages

    var movieId: Long = 0 // ID of the movie for which reviews are loaded

    private fun emitLoadingState() {
        // Update the reviewsState with loading state
        _reviewsState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    // Method to load more reviews
    suspend fun loadMore() {
        // Check if there are more pages to load and not already loading
        if (currentReviewsPage < totalReviewsPages && reviewsState.value.isLoading.not()) {
            currentReviewsPage++
            loadReviews()
        }
    }

    // Method to load reviews
    suspend fun loadReviews() {
        emitLoadingState()
        _reviewsState.update {
            val reviewsState = try {
                val reviewsResponse =
                    repository.getReviews(
                        movieId = movieId,
                        currentPage = currentReviewsPage
                    )
                totalReviewsPages = reviewsResponse.totalPages
                allReviews.addAll(reviewsResponse.reviewNetworks.toReviewList()) // Add new reviews to the set
                ReviewsState(
                    reviews = allReviews.toList(),
                    isLoading = false,
                    errorMessage = ""
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                ReviewsState(
                    reviews = emptyList(),
                    isLoading = false,
                    errorMessage = ex.message.toString()
                )
            }
            it.copy(
                reviews = reviewsState.reviews,
                isLoading = reviewsState.isLoading,
                errorMessage = reviewsState.errorMessage
            )
        }
    }
}

// Data class representing the state of reviews
data class ReviewsState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty() // Method to check if there is an error message
}

// Extension function to convert List<ReviewNetwork> to List<Review>
fun List<ReviewNetwork>.toReviewList(): List<Review> = ArrayList<Review>().apply {
    this@toReviewList.forEach {
        add(it.toUiReview()) // Convert each ReviewNetwork object to Review and add it to the list
    }
}
