package com.example.movierama.di

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.domain.movies.MoviesRepositoryImpl
import com.example.movierama.network.MoviesService
import com.example.movierama.storage.PreferenceManager
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
        service: MoviesService,
        preferenceManager: PreferenceManager
    ): MoviesRepository = MoviesRepositoryImpl(
        dispatchersImpl = dispatchersImpl,
        service = service,
        preferenceManager = preferenceManager
    )
}
