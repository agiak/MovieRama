package com.example.movierama.di

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.domain.search.SearchRepository
import com.example.movierama.domain.search.SearchRepositoryImpl
import com.example.movierama.storage.sharedpreferences.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Singleton
    @Provides
    fun provideContext(
        dispatchersImpl: IDispatchers,
        preferenceManager: PreferenceManager,
    ): SearchRepository = SearchRepositoryImpl(
        dispatchers = dispatchersImpl,
        dataSource = preferenceManager
    )
}