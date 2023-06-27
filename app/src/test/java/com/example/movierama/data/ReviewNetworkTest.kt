package com.example.movierama.data

import com.example.movierama.data.network.reviews.AuthorDetails
import com.example.movierama.data.network.reviews.ReviewNetwork
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ReviewNetworkTest {

    @Test
    fun `toUiReview should convert ReviewNetwork to Review with rounded rating`() {
        // Given data
        val reviewNetwork = ReviewNetwork(
            id = "123",
            author = "John Doe",
            authorDetails = AuthorDetails(
                avatarPath = "avatar_path",
                name = "John Doe",
                rating = 8.6,
                username = "johndoe"
            ),
            content = "This movie is amazing!",
            createdAt = "2023-06-25T12:34:56Z",
            updatedAt = "2023-06-25T12:34:56Z",
            url = "https://www.example.com/review/123"
        )

        // Expected data
        val review = reviewNetwork.toUiReview()

        // Assertions
        assertThat(review.id).isEqualTo("123")
        assertThat(review.author).isEqualTo("John Doe")
        assertThat(review.content).isEqualTo("This movie is amazing!")
        assertThat(review.rating).isEqualTo(4.3) // Rounded rating to 5-star scale
    }
}