package com.example.movierama.di

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.movies.MoviesRepositoryImpl
import com.example.movierama.network.services.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoviesDomainModule {

    @Singleton
    @Provides
    fun provideContext(
        dispatchersImpl: IDispatchers,
        service: MoviesService
    ): MoviesRepository = MoviesRepositoryImpl(
        dispatchersImpl = dispatchersImpl,
        service = service
    )
}
