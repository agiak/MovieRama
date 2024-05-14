package com.example.movierama.features.home.data

import com.example.movierama.core.data.errorhandling.UiMessage
import com.example.movierama.core.data.movies.Movie
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.paging.PagingData

sealed class HomeState {
    object Loading : HomeState()
    data class Error(val error: UiMessage): HomeState()
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
        get() = pagingData.currentItemsList.toList()

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

    fun reset() = pagingData.reset()
}
