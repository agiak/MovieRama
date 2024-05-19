package com.example.movierama.features.home.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.domain.movies.MoviesService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val PAGE_SIZE = 30
private const val FETCH_DISTANCE = 2

class HomeRepositoryImpl @Inject constructor(
    private val service: MoviesService,
): HomeRepository {

    override fun fetchPopularMovies(): Flow<PagingData<MovieNetwork>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
            enablePlaceholders = false,
            prefetchDistance = FETCH_DISTANCE
        ),
        pagingSourceFactory = { HomeDataSource(service = service, moviesType = MoviesType.POPULAR) }
    ).flow

    override fun fetchTopRatedMovies(): Flow<PagingData<MovieNetwork>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
            enablePlaceholders = false,
            prefetchDistance = FETCH_DISTANCE
        ),
        pagingSourceFactory = { HomeDataSource(service = service, moviesType = MoviesType.TOP_RATED) }
    ).flow

    override fun fetchUpcomingMovies(): Flow<PagingData<MovieNetwork>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
            enablePlaceholders = false,
            prefetchDistance = FETCH_DISTANCE
        ),
        pagingSourceFactory = { HomeDataSource(service = service, moviesType = MoviesType.UPCOMING) }
    ).flow

    override fun fetchNowPlayingMovies(): Flow<PagingData<MovieNetwork>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
            enablePlaceholders = false,
            prefetchDistance = FETCH_DISTANCE
        ),
        pagingSourceFactory = { HomeDataSource(service = service, moviesType = MoviesType.NOW_PLAYING) }
    ).flow
}

data class MoviesTypeResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val moviesNetwork: List<MovieNetwork>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    val type: MoviesType
) {
    constructor(moviesResponse: MoviesResponse, type: MoviesType) : this(
        page = moviesResponse.page,
        moviesNetwork = moviesResponse.moviesNetwork,
        totalPages = moviesResponse.totalPages,
        totalResults = moviesResponse.totalPages,
        type = type
    )
}
