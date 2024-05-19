package com.example.movierama.features.movies.di

import com.example.movierama.core.domain.movies.MoviesService
import com.example.movierama.features.movies.domain.MovieTypeRepositoryImpl
import com.example.movierama.features.movies.domain.MoviesTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MoviesTypeModule {

    @Provides
    @ViewModelScoped
    fun provideMovieTypeRepository(moviesService: MoviesService): MoviesTypeRepository =
        MovieTypeRepositoryImpl(moviesService)

}