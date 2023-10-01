package com.example.movierama.di

import com.example.movierama.domain.settings.SettingsRepository
import com.example.movierama.storage.sharedpreferences.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    @Singleton
    @Provides
    fun providesSettingsRepo(preferenceManager: PreferenceManager) =
        SettingsRepository(preferenceManager)
}