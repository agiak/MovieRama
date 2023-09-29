package com.example.movierama.ui.features.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.useCases.favourites.FavouriteUseCase
import com.example.movierama.domain.useCases.moviedetails.CreditsDetails
import com.example.movierama.domain.useCases.moviedetails.CreditsUseCase
import com.example.movierama.domain.useCases.moviedetails.MovieDetailsState
import com.example.movierama.domain.useCases.moviedetails.MovieDetailsUseCase
import com.example.movierama.domain.useCases.moviedetails.ReviewsState
import com.example.movierama.domain.useCases.moviedetails.ReviewsUseCase
import com.example.movierama.domain.useCases.moviedetails.SimilarMoviesState
import com.example.movierama.domain.useCases.moviedetails.SimilarMoviesUseCase
import com.example.movierama.model.toFavouriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases,
    dispatcher: IDispatchers
) : ViewModel() {

    var movieId: Long = 0L

    val movieState: StateFlow<MovieState> = combine(
        movieUseCases.similarMoviesUseCase.similarMoviesState,
        movieUseCases.reviewsUseCase.reviewsState,
        movieUseCases.creditsUseCase.creditsState,
        movieUseCases.movieDetailsUseCase.movieDetailsState
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
        movieUseCases.setMovieIdToUseCases(movieId)
        viewModelScope.async {
            movieUseCases.movieDetailsUseCase.getMovieDetails()
            movieUseCases.creditsUseCase.getCredits()
            movieUseCases.reviewsUseCase.loadReviews()
            movieUseCases.similarMoviesUseCase.loadMovies()
        }
    }

    fun onFavouriteChanged() {
        viewModelScope.launch {
            movieState.value.movieDetailsState.movieDetails?.let { movieDetails ->
                movieUseCases.favouriteUseCase.onFavouriteChanged(movieDetails.toFavouriteMovie())
            }
        }
    }

    fun getMoreSimilarMovies() {
        viewModelScope.launch {
            movieUseCases.similarMoviesUseCase.loadMore()
        }
    }

    fun getMoreReviews() {
        viewModelScope.launch {
            movieUseCases.reviewsUseCase.loadMore()
        }
    }
}

data class MovieState(
    val movieDetailsState: MovieDetailsState,
    val creditsDetails: CreditsDetails,
    val similarMoviesState: SimilarMoviesState,
    val reviewsState: ReviewsState
)

data class MovieUseCases(
    val movieDetailsUseCase: MovieDetailsUseCase,
    val similarMoviesUseCase: SimilarMoviesUseCase,
    val reviewsUseCase: ReviewsUseCase,
    val creditsUseCase: CreditsUseCase,
    val favouriteUseCase: FavouriteUseCase
) {
    fun setMovieIdToUseCases(movieId: Long) {
        movieDetailsUseCase.movieId = movieId
        reviewsUseCase.movieId = movieId
        similarMoviesUseCase.movieId = movieId
        creditsUseCase.movieId = movieId
    }
}
