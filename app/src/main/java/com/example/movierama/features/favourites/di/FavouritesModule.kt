package com.example.movierama.features.favourites.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.domain.storage.localdb.FavouriteMovieDao
import com.example.movierama.features.search.domain.SearchRepositoryImpl
import com.example.movierama.core.domain.storage.sharedpreferences.PreferenceManager
import com.example.movierama.features.details.domain.DetailsRepository
import com.example.movierama.features.details.domain.DetailsRepositoryImpl
import com.example.movierama.features.details.domain.DetailsService
import com.example.movierama.features.favourites.domain.FavouriteRepository
import com.example.movierama.features.favourites.domain.FavouriteRepositoryImpl
import com.example.movierama.features.search.domain.SearchRepository
import com.example.movierama.features.search.domain.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavouritesModule {

    @Singleton
    @Provides
    fun provideFavouritesRepository(
        dispatchersImpl: IDispatchers,
        favouriteMovieDao: FavouriteMovieDao,
    ): FavouriteRepository = FavouriteRepositoryImpl(
        dispatchers = dispatchersImpl,
        localDao = favouriteMovieDao
    )
}
