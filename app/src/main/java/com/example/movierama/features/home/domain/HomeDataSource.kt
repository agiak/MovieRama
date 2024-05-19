package com.example.movierama.features.home.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.domain.movies.MoviesService
import retrofit2.HttpException

private const val STARTING_PAGE_INDEX = 1

class HomeDataSource(
    private val service: MoviesService,
    private val moviesType: MoviesType
) : PagingSource<Int, MovieNetwork>() {

    override fun getRefreshKey(state: PagingState<Int, MovieNetwork>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieNetwork> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val result = getMoviesByType(page)
            LoadResult.Page(
                data = result.moviesNetwork,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (result.moviesNetwork.isEmpty()) null else page + 1
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

    private suspend fun getMoviesByType(page: Int): MoviesResponse {
        return when (moviesType) {
            MoviesType.POPULAR -> {
                service.getPopularMovies(page = page)
            }
            MoviesType.NOW_PLAYING -> {
                service.getNowPlayingMovies(page = page)
            }
            MoviesType.TOP_RATED -> {
                service.getTopRatedMovies(page = page)
            }
            MoviesType.UPCOMING -> {
                service.getUpcomingMovies(page = page)
            }
        }
    }
}
