package com.example.movierama.storage.di

import android.content.Context
import androidx.room.Room
import com.example.movierama.storage.domain.FAVOURITE_DB
import com.example.movierama.storage.domain.localdb.FavouriteMovieDao
import com.example.movierama.storage.domain.localdb.FavouriteMoviesDatabase
import com.example.movierama.storage.domain.sharedpreferences.PreferenceManager
import com.example.movierama.storage.domain.sharedpreferences.PreferenceManagerImpl
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
            FavouriteMoviesDatabase::class.java, FAVOURITE_DB
        ).build()

    @Singleton
    @Provides
    fun provideFavouriteDao(db: FavouriteMoviesDatabase): FavouriteMovieDao = db.favouriteMovieDao()
}
