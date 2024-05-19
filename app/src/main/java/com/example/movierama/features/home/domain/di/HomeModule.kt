package com.example.movierama.features.home.domain.di

import com.example.movierama.core.domain.movies.MoviesService
import com.example.movierama.features.home.domain.HomeRepository
import com.example.movierama.features.home.domain.HomeRepositoryImpl
import com.example.movierama.features.movies.domain.MovieTypeRepositoryImpl
import com.example.movierama.features.movies.domain.MoviesTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class HomeModule {

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(moviesService: MoviesService): HomeRepository =
        HomeRepositoryImpl(moviesService)

}
