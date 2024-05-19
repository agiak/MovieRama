package com.example.movierama.features.movies.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.domain.dispatchers.DispatchersImpl
import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.core.domain.movies.MoviesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 30
private const val FETCH_DISTANCE = 2

class MovieTypeRepositoryImpl @Inject constructor(
    private val moviesService: MoviesService,
) : MoviesTypeRepository {

    override fun getMovies(moviesType: MoviesType): Flow<PagingData<MovieNetwork>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false,
                prefetchDistance = FETCH_DISTANCE
            ),
            pagingSourceFactory = { MoviesTypeDataSource(service = moviesService, moviesType = moviesType)}
        ).flow
    }
}