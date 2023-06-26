package com.example.movierama.di

import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.useCases.CreditsUseCase
import com.example.movierama.domain.useCases.MovieDetailsUseCase
import com.example.movierama.domain.useCases.ReviewsUseCase
import com.example.movierama.domain.useCases.SimilarMoviesUseCase
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
    fun provideSimilarMoviesUseCase(repository: MoviesRepository) = SimilarMoviesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideReviewsUseCase(repository: MoviesRepository) = ReviewsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideMovieDetailsUseCase(repository: MoviesRepository) = MovieDetailsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCreditsUseCase(repository: MoviesRepository) = CreditsUseCase(repository)
}
