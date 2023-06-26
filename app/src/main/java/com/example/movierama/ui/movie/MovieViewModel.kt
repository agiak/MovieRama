package com.example.movierama.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.CreditsDetails
import com.example.movierama.domain.useCases.CreditsUseCase
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
    private val repository: MoviesRepository,
    private val similarMoviesUseCase: SimilarMoviesUseCase,
    private val reviewsUseCase: ReviewsUseCase,
    private val movieDetailsUseCase: MovieDetailsUseCase,
    private val creditsUseCase: CreditsUseCase,
    dispatcher: IDispatchers
) : ViewModel() {

    var movieId: Long = 0L

    val movieState: StateFlow<MovieState> = combine(
        similarMoviesUseCase.similarMoviesState,
        reviewsUseCase.reviewsState,
        creditsUseCase.creditsState,
        movieDetailsUseCase.movieDetailsState
    ) { similarMoviesState: SimilarMoviesState, reviewsState: ReviewsState, creditsDetails: CreditsDetails, movieDetailsState: MovieDetailsState ->
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
                initialValue =  MovieState(
                    movieDetailsState = MovieDetailsState(),
                    similarMoviesState = SimilarMoviesState(),
                    reviewsState = ReviewsState(),
                    creditsDetails = CreditsDetails()
                )
        )


    fun getData() {
        movieDetailsUseCase.movieId = this@MovieViewModel.movieId
        reviewsUseCase.movieId = this@MovieViewModel.movieId
        similarMoviesUseCase.movieId = this@MovieViewModel.movieId
        creditsUseCase.movieId = this@MovieViewModel.movieId
        viewModelScope.async {
            movieDetailsUseCase.getMovieDetails()
            creditsUseCase.getCredits()
            reviewsUseCase.loadReviews()
            similarMoviesUseCase.loadMovies()
        }
    }

    fun onFavouriteChanged() {
        repository.onFavouriteChange(movieId)
    }

    fun getMoreSimilarMovies() {
        viewModelScope.launch {
            similarMoviesUseCase.loadMore()
        }
    }

    fun getMoreReviews() {
        viewModelScope.launch {
            reviewsUseCase.loadMore()
        }
    }
}

data class MovieState(
    val movieDetailsState: MovieDetailsState,
    val creditsDetails: CreditsDetails,
    val similarMoviesState: SimilarMoviesState,
    val reviewsState: ReviewsState
)
