package com.example.movierama.di

import com.example.movierama.domain.error_hadling.ErrorHandler
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.CreditsUseCase
import com.example.movierama.domain.useCases.FavouriteUseCase
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.domain.useCases.MovieDetailsUseCase
import com.example.movierama.domain.useCases.ReviewsUseCase
import com.example.movierama.domain.useCases.SearchMovieUseCase
import com.example.movierama.domain.useCases.SimilarMoviesUseCase
import com.example.movierama.ui.movie.MovieUseCases
import com.example.movierama.ui.movies.MoviesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MoviesModule {

    @Provides
    @ViewModelScoped
    fun provideSimilarMoviesUseCase(repository: MoviesRepository, errorHandler: ErrorHandler)
        = SimilarMoviesUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideReviewsUseCase(repository: MoviesRepository, errorHandler: ErrorHandler)
        = ReviewsUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideMovieDetailsUseCase(repository: MoviesRepository, errorHandler: ErrorHandler)
        = MovieDetailsUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideCreditsUseCase(repository: MoviesRepository) = CreditsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideFavouriteUseCase(repository: MoviesRepository) = FavouriteUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideFetchMoviesUseCase(repository: MoviesRepository) = FetchMoviesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSearchMovieUseCase(repository: MoviesRepository) = SearchMovieUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideMoviesUseCases(
        fetchMoviesUseCase: FetchMoviesUseCase,
        searchMovieUseCase: SearchMovieUseCase,
        favouriteUseCase: FavouriteUseCase
    ) = MoviesUseCases(
        fetchMoviesUseCase = fetchMoviesUseCase,
        searchMovieUseCase = searchMovieUseCase,
        favouriteUseCase = favouriteUseCase
    )

    @Provides
    @ViewModelScoped
    fun provideMovieUseCases(
        movieDetailsUseCase: MovieDetailsUseCase,
        similarMoviesUseCase: SimilarMoviesUseCase,
        reviewsUseCase: ReviewsUseCase,
        creditsUseCase: CreditsUseCase,
        favouriteUseCase: FavouriteUseCase
    ) = MovieUseCases(
        movieDetailsUseCase = movieDetailsUseCase,
        similarMoviesUseCase = similarMoviesUseCase,
        reviewsUseCase = reviewsUseCase,
        creditsUseCase = creditsUseCase,
        favouriteUseCase = favouriteUseCase
    )

}
