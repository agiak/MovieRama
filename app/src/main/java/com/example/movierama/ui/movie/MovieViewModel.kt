package com.example.movierama.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.data.MovieDetails
import com.example.movierama.data.network.reviews.Review
import com.example.movierama.data.network.reviews.ReviewNetwork
import com.example.movierama.data.network.similar.SimilarMovie
import com.example.movierama.data.network.similar.SimilarMovieNetwork
import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    var movieId: Long = 0L

    private val _movieState = MutableStateFlow(
        MovieState(
            movieDetailsState = MovieDetailsState(),
            similarMoviesState = SimilarMoviesState(),
            reviewsState = ReviewsState()
        )
    )
    val movieState: StateFlow<MovieState> = _movieState.asStateFlow()

    init {
        getMovieDetails()
        getReviews()
        getSimilarMovies()
    }

    fun getReviews() {
        viewModelScope.launch {
            _movieState.value.copy(
                reviewsState = try {
                    val reviewsResponse =
                        repository.getReviews(movieId = movieId, currentPage = 1)
                    ReviewsState(
                        reviews = reviewsResponse.reviewNetworks.toReviewList(),
                        isLoading = false,
                        errorMessage = ""
                    )
                } catch (ex: Exception) {
                    ReviewsState(
                        reviews = emptyList(),
                        isLoading = false,
                        errorMessage = ex.message.toString()
                    )
                }
            )
        }
    }

    fun getMovieDetails() {
        viewModelScope.launch {
            _movieState.value.copy(
                movieDetailsState = try {
                    val movieResponse =
                        repository.getMovie(movieId = movieId)
                    MovieDetailsState(
                        movieDetails = movieResponse.toMovieDetails(),
                        isLoading = false,
                        errorMessage = ""
                    )
                } catch (ex: Exception) {
                    MovieDetailsState(
                        movieDetails = null,
                        isLoading = false,
                        errorMessage = ex.message.toString()
                    )
                }
            )
        }
    }

    fun getSimilarMovies() {
        viewModelScope.launch {
            _movieState.value.copy(
                similarMoviesState = try {
                    val similarResponse =
                        repository.getSimilarMovies(movieId = movieId, currentPage = 1)
                    SimilarMoviesState(
                        similarMovies = similarResponse.similarMovieNetworks.toSimilarMovieList(),
                        isLoading = false,
                        errorMessage = ""
                    )
                } catch (ex: Exception) {
                    SimilarMoviesState(
                        similarMovies = emptyList(),
                        isLoading = false,
                        errorMessage = ex.message.toString()
                    )
                }
            )
        }
    }
}

fun List<ReviewNetwork>.toReviewList(): List<Review> = ArrayList<Review>().apply {
    this@toReviewList.forEach {
        add(it.toUiReview())
    }
}

fun List<SimilarMovieNetwork>.toSimilarMovieList(): List<SimilarMovie> = ArrayList<SimilarMovie>().apply {
    this@toSimilarMovieList.forEach {
        add(it.toUiSimilarMovie())
    }
}

data class MovieState(
    val movieDetailsState: MovieDetailsState,
    val similarMoviesState: SimilarMoviesState,
    val reviewsState: ReviewsState
)

data class MovieDetailsState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = true,
    val errorMessage: String = ""
){
    fun hasError() = errorMessage.isNotEmpty()
}

data class SimilarMoviesState(
    val similarMovies: List<SimilarMovie> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
){
    fun hasError() = errorMessage.isNotEmpty()
}

data class ReviewsState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
){
    fun hasError() = errorMessage.isNotEmpty()
}