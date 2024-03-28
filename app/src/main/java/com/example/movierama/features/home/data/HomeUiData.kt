package com.example.movierama.features.home.data

import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.paging.PagingData
import com.example.movierama.network.data.ApiError

sealed class HomeState {
    object Loading : HomeState()
    data class Error(val error: ApiError): HomeState()
    data class Result(val data: List<HomeMovieTypeList> = emptyList()) : HomeState()
    data class FetchingMore(val movies: List<Movie>, val moviesType: MoviesType) : HomeState()
}

data class HomeMovieTypeList(
    val label: String,
    val movies: List<Movie> = emptyList(),
    val moviesType: MoviesType,
)

class HomePagingData(
    var pagingData: PagingData<Movie> = PagingData(),
    var isLoading: Boolean = false
) {
    val movies: List<Movie>
        get() = pagingData.currentMoviesList.toList()

    val canFetchMore: Boolean
        get() = pagingData.canFetchMore()

    val currentPage: Int
        get() = pagingData.currentPage

    operator fun inc(): HomePagingData {
        pagingData++
        return this
    }

    val isFetching: Boolean
        get() = isLoading

    fun setPagingData(totalPages: Int, newItems: List<Movie>) {
        pagingData.apply {
            this.totalPages = totalPages
            currentMoviesList.addAll(newItems)
        }
    }

    fun reset() = pagingData.reset()
}
