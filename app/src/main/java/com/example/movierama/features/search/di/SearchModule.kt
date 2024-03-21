package com.example.movierama.features.search.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.search.domain.SearchRepository
import com.example.movierama.features.search.domain.SearchRepositoryImpl
import com.example.movierama.core.domain.storage.sharedpreferences.PreferenceManager
import com.example.movierama.features.search.domain.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Singleton
    @Provides
    fun provideContext(
        dispatchersImpl: IDispatchers,
        preferenceManager: PreferenceManager,
        searchService: SearchService
    ): SearchRepository = SearchRepositoryImpl(
        dispatchers = dispatchersImpl,
        dataSource = preferenceManager,
        service = searchService
    )

    @Singleton
    @Provides
    fun provideSearchService(
        retrofit: Retrofit
    ): SearchService = retrofit.create(SearchService::class.java)
}