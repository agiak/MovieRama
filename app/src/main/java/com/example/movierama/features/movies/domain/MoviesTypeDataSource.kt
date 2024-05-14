package com.example.movierama.features.movies.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movierama.core.data.movies.MovieNetwork
import com.example.movierama.core.data.movies.MoviesResponse
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.movies.MoviesType.NOW_PLAYING
import com.example.movierama.core.data.movies.MoviesType.POPULAR
import com.example.movierama.core.data.movies.MoviesType.TOP_RATED
import com.example.movierama.core.data.movies.MoviesType.UPCOMING
import com.example.movierama.core.domain.movies.MoviesService
import kotlinx.coroutines.delay
import retrofit2.HttpException

private const val STARTING_PAGE_INDEX = 1

class MoviesTypeDataSource(
    private val service: MoviesService,
    private val moviesType: MoviesType
) : PagingSource<Int, MovieNetwork>() {

    override fun getRefreshKey(state: PagingState<Int, MovieNetwork>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
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
        return when(moviesType) {
            POPULAR -> service.getPopularMovies(page= page)
            NOW_PLAYING -> service.getNowPlayingMovies(page= page)
            TOP_RATED -> service.getTopRatedMovies(page= page)
            UPCOMING -> service.getUpcomingMovies(page= page)
        }
    }

}
