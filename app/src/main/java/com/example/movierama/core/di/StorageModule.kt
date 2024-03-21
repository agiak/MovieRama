package com.example.movierama.core.di

import android.content.Context
import androidx.room.Room
import com.example.movierama.core.domain.storage.localdb.FavouriteMovieDao
import com.example.movierama.core.domain.storage.localdb.FavouriteMoviesDatabase
import com.example.movierama.core.domain.storage.sharedpreferences.PreferenceManager
import com.example.movierama.core.domain.storage.sharedpreferences.PreferenceManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager =
        PreferenceManagerImpl(context)

    @Singleton
    @Provides
    fun provideFavouriteMoviesDatabase(@ApplicationContext applicationContext: Context): FavouriteMoviesDatabase =
        Room.databaseBuilder(
            applicationContext,
            FavouriteMoviesDatabase::class.java, "favourite_movies"
        ).build()

    @Singleton
    @Provides
    fun provideFavouriteDao(db: FavouriteMoviesDatabase): FavouriteMovieDao = db.favouriteMovieDao()
}
