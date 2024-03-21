package com.example.movierama.core.di.movies

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.domain.movies.MoviesRepository
import com.example.movierama.core.domain.movies.MoviesRepositoryImpl
import com.example.movierama.core.domain.movies.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoviesModule {

    @Singleton
    @Provides
    fun provideContext(
        dispatchersImpl: IDispatchers,
        service: MoviesService,
    ): MoviesRepository = MoviesRepositoryImpl(
        dispatchers = dispatchersImpl,
        service = service,
    )
}