package com.example.movierama.domain.useCases

import com.example.movierama.data.network.reviews.Review
import com.example.movierama.data.network.reviews.ReviewNetwork
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

    private val _reviewsState = MutableStateFlow(ReviewsState())
    val reviewsState: StateFlow<ReviewsState> = _reviewsState.asStateFlow()

    private val allReviews: MutableSet<Review> = mutableSetOf()
    private var currentReviewsPage = 1
    private var totalReviewsPages = 1

    var movieId: Long = 0

    private fun emitLoadingState() {
        _reviewsState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    suspend fun loadMore() {
        if (currentReviewsPage < totalReviewsPages && reviewsState.value.isLoading.not()) {
            currentReviewsPage++
            loadReviews()
        }
    }

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
                allReviews.addAll(reviewsResponse.reviewNetworks.toReviewList())
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

data class ReviewsState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}

fun List<ReviewNetwork>.toReviewList(): List<Review> = ArrayList<Review>().apply {
    this@toReviewList.forEach {
        add(it.toUiReview())
    }
}
