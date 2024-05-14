package com.example.movierama.core.data.paging

data class PagingData<T>(
    var currentPage: Int = 1,
    var totalPages: Int = 1,
    var currentItemsList: MutableSet<T> = mutableSetOf(),
) {
    fun reset() {
        currentPage = 1
        totalPages = 1
        currentItemsList.clear()
    }

    operator fun inc(): PagingData<T> {
        currentPage++
        return this
    }

    fun setData(totalPages: Int, newItems: List<T>) {
        this.totalPages = totalPages
        currentItemsList.addAll(newItems)
    }

    fun canFetchMore() = currentPage < totalPages
}
