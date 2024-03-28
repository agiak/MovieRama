package com.example.movierama.features.favourites.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.favourites.domain.FavouriteRepository
import com.example.movierama.features.favourites.domain.FavouriteRepositoryImpl
import com.example.movierama.storage.domain.localdb.FavouriteMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
