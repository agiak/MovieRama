package com.example.movierama.features.details.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.details.domain.DetailsRepository
import com.example.movierama.features.details.domain.DetailsRepositoryImpl
import com.example.movierama.features.details.domain.DetailsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DetailsModule {

    @Singleton
    @Provides
    fun provideDetailsRepository(
        dispatchersImpl: IDispatchers,
        service: DetailsService
    ): DetailsRepository = DetailsRepositoryImpl(
        dispatchers = dispatchersImpl,
        service = service
    )

    @Singleton
    @Provides
    fun provideDetailsService(
        retrofit: Retrofit
    ): DetailsService = retrofit.create(DetailsService::class.java)
}
