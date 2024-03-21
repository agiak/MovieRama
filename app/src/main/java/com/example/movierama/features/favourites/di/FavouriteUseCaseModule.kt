package com.example.movierama.features.favourites.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.domain.storage.localdb.FavouriteMovieDao
import com.example.movierama.features.favourites.domain.FavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class FavouriteUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideFavouriteUseCase(
        favouriteMovieDao: FavouriteMovieDao,
        dispatchers: IDispatchers
    ) = FavouriteUseCase(favouriteMovieDao, dispatchers)
}