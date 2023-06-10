package com.example.movierama.di

import android.content.Context
import com.example.movierama.storage.PreferenceManager
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
    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)
}