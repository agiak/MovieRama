package com.example.movierama.features.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.core.data.movies.toStoredFavouriteMovie
import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.details.domain.usecases.CreditsDetails
import com.example.movierama.features.details.domain.usecases.CreditsUseCase
import com.example.movierama.features.details.domain.usecases.MovieDetailsState
import com.example.movierama.features.details.domain.usecases.MovieDetailsUseCase
import com.example.movierama.features.details.domain.usecases.ReviewsState
import com.example.movierama.features.details.domain.usecases.ReviewsUseCase
import com.example.movierama.features.details.domain.usecases.SimilarMoviesState
import com.example.movierama.features.details.domain.usecases.SimilarMoviesUseCase
import com.example.movierama.features.favourites.domain.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsUseCases: MovieDetailsUseCases,
    private val favouriteRepository: FavouriteRepository,
    dispatcher: IDispatchers,
) : ViewModel() {

    var movieId: Long = 0L

    val movieState: StateFlow<MovieState> = combine(
        movieDetailsUseCases.similarMoviesUseCase.similarMoviesState,
        movieDetailsUseCases.reviewsUseCase.reviewsState,
        movieDetailsUseCases.creditsUseCase.creditsState,
        movieDetailsUseCases.movieDetailsUseCase.movieDetailsState,
    ) { similarMoviesState: SimilarMoviesState,
        reviewsState: ReviewsState,
        creditsDetails: CreditsDetails,
        movieDetailsState: MovieDetailsState ->

        MovieState(
            similarMoviesState = similarMoviesState,
            reviewsState = reviewsState,
            movieDetailsState = movieDetailsState,
            creditsDetails = creditsDetails
        )
    }
        .flowOn(dispatcher.defaultThread())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MovieState(
                movieDetailsState = MovieDetailsState(),
                similarMoviesState = SimilarMoviesState(),
                reviewsState = ReviewsState(),
                creditsDetails = CreditsDetails()
            )
        )

    @Suppress("DeferredResultUnused")
    fun getData() {
        movieDetailsUseCases.setMovieIdToUseCases(movieId)
        viewModelScope.launch {
            movieDetailsUseCases.movieDetailsUseCase.getMovieDetails()
            movieDetailsUseCases.creditsUseCase.getCredits()
            movieDetailsUseCases.reviewsUseCase.loadReviews()
            movieDetailsUseCases.similarMoviesUseCase.loadMovies()
        }
    }

    fun onFavouriteChanged() {
        viewModelScope.launch {
            movieState.value.movieDetailsState.movieDetails?.let { movieDetails ->
                favouriteRepository.onFavouriteStatusChanged(movieDetails.toStoredFavouriteMovie())
            }
        }
    }

    fun getMoreSimilarMovies() {
        viewModelScope.launch {
            movieDetailsUseCases.similarMoviesUseCase.loadMore()
        }
    }

    fun getMoreReviews() {
        viewModelScope.launch {
            movieDetailsUseCases.reviewsUseCase.loadMore()
        }
    }
}

data class MovieState(
    val movieDetailsState: MovieDetailsState,
    val creditsDetails: CreditsDetails,
    val similarMoviesState: SimilarMoviesState,
    val reviewsState: ReviewsState
)

data class MovieDetailsUseCases(
    val movieDetailsUseCase: MovieDetailsUseCase,
    val similarMoviesUseCase: SimilarMoviesUseCase,
    val reviewsUseCase: ReviewsUseCase,
    val creditsUseCase: CreditsUseCase,
) {
    fun setMovieIdToUseCases(movieId: Long) {
        movieDetailsUseCase.movieId = movieId
        reviewsUseCase.movieId = movieId
        similarMoviesUseCase.movieId = movieId
        creditsUseCase.movieId = movieId
    }
}
