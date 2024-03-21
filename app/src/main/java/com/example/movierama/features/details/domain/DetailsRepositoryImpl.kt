package com.example.movierama.features.details.domain

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.details.data.CreditsResponse
import com.example.movierama.features.details.data.MovieDetailsResponse
import com.example.movierama.features.details.data.ReviewsResponse
import com.example.movierama.features.details.data.SimilarResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val dispatchers: IDispatchers,
    private val service: DetailsService
) : DetailsRepository {

    override suspend fun getMovie(movieId: Long): MovieDetailsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getMovieDetails(movieId)
        }

    override suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getReviews(movieId = movieId, page = currentPage)
        }

    override suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getSimilarMovies(movieId = movieId, page = currentPage)
        }

    override suspend fun getMovieCredits(movieId: Long): CreditsResponse =
        withContext(dispatchers.backgroundThread()) {
            service.getMovieCredits(movieId = movieId)
        }
}
