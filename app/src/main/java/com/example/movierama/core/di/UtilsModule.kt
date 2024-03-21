package com.example.movierama.core.di

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.presentation.utils.DebounceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    fun providesCoroutineDispatcherIO(dispatchersImpl: IDispatchers): DebounceUtil =
        DebounceUtil(dispatcher = dispatchersImpl)
}
