package com.example.movierama.core.di

import android.content.Context
import com.example.movierama.core.domain.errorhadling.ErrorHandler
import com.example.movierama.core.domain.errorhadling.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ErrorModule {

    @Provides
    fun providesErrorHandler(context: Context): ErrorHandler =
        ErrorHandlerImpl(context)
}
