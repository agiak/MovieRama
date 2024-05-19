package com.example.movierama.features.home.domain

import androidx.paging.PagingData
import com.example.movierama.core.data.movies.MovieNetwork
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun fetchPopularMovies(): Flow<PagingData<MovieNetwork>>
    fun fetchTopRatedMovies(): Flow<PagingData<MovieNetwork>>
    fun fetchUpcomingMovies(): Flow<PagingData<MovieNetwork>>
    fun fetchNowPlayingMovies(): Flow<PagingData<MovieNetwork>>
}
