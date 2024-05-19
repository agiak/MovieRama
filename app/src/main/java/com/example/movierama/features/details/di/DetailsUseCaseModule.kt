package com.example.movierama.features.details.di

import com.example.movierama.core.domain.errorhadling.ErrorHandler
import com.example.movierama.features.details.domain.DetailsRepository
import com.example.movierama.features.details.domain.usecases.CreditsUseCase
import com.example.movierama.features.details.domain.usecases.MovieDetailsUseCase
import com.example.movierama.features.details.domain.usecases.ReviewsUseCase
import com.example.movierama.features.details.domain.usecases.SimilarMoviesUseCase
import com.example.movierama.features.favourites.domain.FavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailsUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideSimilarMoviesUseCase(repository: DetailsRepository, errorHandler: ErrorHandler) =
        SimilarMoviesUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideReviewsUseCase(repository: DetailsRepository, errorHandler: ErrorHandler) =
        ReviewsUseCase(repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideMovieDetailsUseCase(
        favouriteUseCase: FavouriteUseCase,
        repository: DetailsRepository,
        errorHandler: ErrorHandler
    ) = MovieDetailsUseCase(favouriteUseCase, repository, errorHandler)

    @Provides
    @ViewModelScoped
    fun provideCreditsUseCase(repository: DetailsRepository) = CreditsUseCase(repository)
}