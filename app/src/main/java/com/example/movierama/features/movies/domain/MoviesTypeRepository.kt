package com.example.movierama.features.movies.domain

import androidx.paging.PagingData
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesType
import kotlinx.coroutines.flow.Flow

interface MoviesTypeRepository {

    fun fetchMovies(moviesType: MoviesType): Flow<PagingData<MovieNetwork>>
}