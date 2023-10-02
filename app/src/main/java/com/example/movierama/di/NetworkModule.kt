package com.example.movierama.di

import android.content.Context
import com.example.movierama.BuildConfig
import com.example.movierama.network.ConnectionController
import com.example.movierama.network.ConnectionControllerImpl
import com.example.movierama.network.interceptors.AuthInterceptor
import com.example.movierama.network.interceptors.ErrorHandlerInterceptor
import com.example.movierama.network.services.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        errorHandlerInterceptor: ErrorHandlerInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(authInterceptor)
            .addInterceptor(errorHandlerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesErrorHandlerInterceptor(connectionController: ConnectionController) =
        ErrorHandlerInterceptor(connectionController)

    @Provides
    @Singleton
    fun providesConnectionController(@ApplicationContext context: Context): ConnectionController =
        ConnectionControllerImpl(context)

    @Provides
    @Singleton
    fun providesFullLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideProductService(retrofit: Retrofit): MoviesService =
        retrofit.create(MoviesService::class.java)
}
