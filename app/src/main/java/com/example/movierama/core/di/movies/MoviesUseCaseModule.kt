package com.example.movierama.core.di.movies

import com.example.movierama.core.domain.movies.MoviesRepository
import com.example.movierama.core.domain.movies.usecases.FetchMoviesUseCase
import com.example.movierama.features.details.domain.usecases.CreditsUseCase
import com.example.movierama.features.details.domain.usecases.MovieDetailsUseCase
import com.example.movierama.features.details.domain.usecases.ReviewsUseCase
import com.example.movierama.features.details.domain.usecases.SimilarMoviesUseCase
import com.example.movierama.features.details.presentation.MovieDetailsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MoviesUseCaseModule {

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
    ) = MovieDetailsUseCases(
        movieDetailsUseCase = movieDetailsUseCase,
        similarMoviesUseCase = similarMoviesUseCase,
        reviewsUseCase = reviewsUseCase,
        creditsUseCase = creditsUseCase,
    )

}