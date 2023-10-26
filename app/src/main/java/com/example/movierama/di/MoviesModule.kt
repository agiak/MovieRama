package com.example.movierama.di

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.error_hadling.ErrorHandler
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.FetchMoviesUseCase
import com.example.movierama.domain.useCases.favourites.FavouriteUseCase
import com.example.movierama.domain.useCases.moviedetails.CreditsUseCase
import com.example.movierama.domain.useCases.moviedetails.MovieDetailsUseCase
import com.example.movierama.domain.useCases.moviedetails.ReviewsUseCase
import com.example.movierama.domain.useCases.moviedetails.SimilarMoviesUseCase
import com.example.movierama.storage.localdb.FavouriteMovieDao
import com.example.movierama.ui.features.movie.MovieUseCases
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
    fun provideSimilarMoviesUseCase(repository: MoviesRepository, errorHandler: ErrorHandler) =
        SimilarMoviesUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideReviewsUseCase(repository: MoviesRepository, errorHandler: ErrorHandler) =
        ReviewsUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideMovieDetailsUseCase(
        favouriteUseCase: FavouriteUseCase,
        repository: MoviesRepository,
        errorHandler: ErrorHandler
    ) =
        MovieDetailsUseCase(favouriteUseCase, repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideCreditsUseCase(repository: MoviesRepository) = CreditsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideFavouriteUseCase(
        favouriteMovieDao: FavouriteMovieDao,
        dispatchers: IDispatchers
    ) = FavouriteUseCase(favouriteMovieDao, dispatchers)

    @Provides
    @ViewModelScoped
    fun provideFetchMoviesUseCase(repository: MoviesRepository) = FetchMoviesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideMovieUseCases(
        movieDetailsUseCase: MovieDetailsUseCase,
        similarMoviesUseCase: SimilarMoviesUseCase,
        reviewsUseCase: ReviewsUseCase,
        creditsUseCase: CreditsUseCase,
    ) = MovieUseCases(
        movieDetailsUseCase = movieDetailsUseCase,
        similarMoviesUseCase = similarMoviesUseCase,
        reviewsUseCase = reviewsUseCase,
        creditsUseCase = creditsUseCase,
    )

}
