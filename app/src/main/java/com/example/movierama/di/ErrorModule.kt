package com.example.movierama.di

import android.content.Context
import com.example.movierama.domain.error_hadling.ErrorHandler
import com.example.movierama.domain.error_hadling.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ErrorModule {

    @Provides
    fun providesCoroutineDispatcherIO(context: Context): ErrorHandler =
        ErrorHandlerImpl(context)
}
