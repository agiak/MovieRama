package com.example.movierama.di

import com.example.movierama.network.NetworkInterceptor
import com.example.movierama.BuildConfig
import com.example.movierama.data.network.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        networkInterceptor: NetworkInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesFullLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideProductService(retrofit: Retrofit): MoviesService = retrofit.create(MoviesService::class.java)
}