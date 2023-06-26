package com.example.movierama.di

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.ui.utils.DebounceUtil
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
