package com.example.movierama.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.useCases.CreditsDetails
import com.example.movierama.domain.useCases.CreditsUseCase
import com.example.movierama.domain.useCases.FavouriteUseCase
import com.example.movierama.domain.useCases.MovieDetailsState
import com.example.movierama.domain.useCases.MovieDetailsUseCase
import com.example.movierama.domain.useCases.ReviewsState
import com.example.movierama.domain.useCases.ReviewsUseCase
import com.example.movierama.domain.useCases.SimilarMoviesState
import com.example.movierama.domain.useCases.SimilarMoviesUseCase
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
class MovieViewModel @Inject constructor(
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
        movieUseCases.favouriteUseCase.onFavouriteChanged(movieId)
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
