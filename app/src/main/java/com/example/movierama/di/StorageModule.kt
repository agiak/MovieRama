package com.example.movierama.di

import android.content.Context
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

/*    @Singleton
    @Provides
    fun provideSessionDatabase(@ApplicationContext applicationContext: Context): MyDatabase = Room.databaseBuilder(
        applicationContext,
        MyDatabase::class.java, "my_database"
    ).build()*/
}