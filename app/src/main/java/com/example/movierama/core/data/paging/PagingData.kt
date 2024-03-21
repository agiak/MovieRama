package com.example.movierama.core.data.paging

data class PagingData<T>(
    var currentPage: Int = 1,
    var totalPages: Int = 1,
    var currentMoviesList: MutableSet<T> = mutableSetOf(),
) {
    fun reset() {
        currentPage = 1
        totalPages = 1
        currentMoviesList.clear()
    }

    operator fun inc(): PagingData<T> {
        currentPage++
        return this
    }

    fun canFetchMore() = currentPage < totalPages
}
