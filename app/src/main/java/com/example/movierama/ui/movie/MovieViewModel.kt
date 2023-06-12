package com.example.movierama.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.data.CreditsDetails
import com.example.movierama.data.MovieDetails
import com.example.movierama.data.network.reviews.Review
import com.example.movierama.data.network.reviews.ReviewNetwork
import com.example.movierama.data.network.similar.SimilarMovie
import com.example.movierama.data.network.similar.SimilarMovieNetwork
import com.example.movierama.domain.movies.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
            reviewsState = ReviewsState(),
            creditsDetails = CreditsDetails()
        )
    )
    val movieState: StateFlow<MovieState> = _movieState

    private val allReviews: MutableSet<Review> = mutableSetOf()
    private var currentReviewsPage = 1
    private var totalReviewsPages = 1

    private val allSimilarMovies: MutableSet<SimilarMovie> = mutableSetOf()
    private var currentSimilarMoviesPage = 1
    private var totalSimilarMoviesPages = 1

    fun getData() {
        getMovieDetails()
        getReviews()
        getSimilarMovies()
        getCredits()
    }

    private fun getCredits() {
        viewModelScope.launch {
            _movieState.update {
                it.copy(
                    creditsDetails = try {
                        val creditsResponse = repository.getMovieCredits(movieId)
                        CreditsDetails(
                            director = creditsResponse.getDirector(),
                            cast = creditsResponse.getCast()
                        )
                    } catch (ex: Exception) {
                        CreditsDetails()
                    }
                )
            }
        }
    }

    fun getReviews() {
        viewModelScope.launch {
            _movieState.update {
                it.copy(
                    reviewsState = try {
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
                )
            }
        }
    }

    fun getMoreReviews() {
        if (currentReviewsPage < totalReviewsPages) {
            currentReviewsPage++
            getReviews()
        }
    }

    fun getMovieDetails() {
        viewModelScope.launch {
            _movieState.update {
                it.copy(
                    movieDetailsState = try {
                        val movieResponse =
                            repository.getMovie(movieId = movieId)
                        MovieDetailsState(
                            movieDetails = movieResponse.toMovieDetails().also { movieDetails ->
                                movieDetails.setIsFavouriteToMovieDetails()
                            },
                            isLoading = false,
                            errorMessage = ""
                        )
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        MovieDetailsState(
                            movieDetails = null,
                            isLoading = false,
                            errorMessage = ex.message.toString()
                        )
                    }
                )
            }
        }
    }

    fun onFavouriteChanged() {
        repository.onFavouriteChange(movieId)
    }

    private fun MovieDetails.setIsFavouriteToMovieDetails() {
        isFavourite = repository.isMovieFavourite(id)
    }

    fun getSimilarMovies() {
        viewModelScope.launch {
            _movieState.update {
                it.copy(
                    similarMoviesState = try {
                        val similarResponse =
                            repository.getSimilarMovies(
                                movieId = movieId,
                                currentPage = currentSimilarMoviesPage
                            )
                        totalSimilarMoviesPages = similarResponse.totalPages
                        allSimilarMovies.addAll(similarResponse.similarMovieNetworks.toSimilarMovieList())
                        SimilarMoviesState(
                            similarMovies = allSimilarMovies.toList(),
                            isLoading = false,
                            errorMessage = ""
                        )
                    } catch (ex: Exception) {
                        ex.printStackTrace()
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

    fun getMoreSimilarMovies() {
        if (currentSimilarMoviesPage < totalSimilarMoviesPages) {
            currentSimilarMoviesPage++
            getSimilarMovies()
        }
    }
}

fun List<ReviewNetwork>.toReviewList(): List<Review> = ArrayList<Review>().apply {
    this@toReviewList.forEach {
        add(it.toUiReview())
    }
}

fun List<SimilarMovieNetwork>.toSimilarMovieList(): List<SimilarMovie> =
    ArrayList<SimilarMovie>().apply {
        this@toSimilarMovieList.forEach {
            add(it.toUiSimilarMovie())
        }
    }

data class MovieState(
    val movieDetailsState: MovieDetailsState,
    val creditsDetails: CreditsDetails,
    val similarMoviesState: SimilarMoviesState,
    val reviewsState: ReviewsState
)

data class MovieDetailsState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}

data class SimilarMoviesState(
    val similarMovies: List<SimilarMovie> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}

data class ReviewsState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun hasError() = errorMessage.isNotEmpty()
}
