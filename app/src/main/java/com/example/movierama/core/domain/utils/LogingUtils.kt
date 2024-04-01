package com.example.movierama.core.domain.utils

import com.example.movierama.core.data.movies.MoviesType
import timber.log.Timber

fun MoviesType.logPagingStart(page: Int) {
    Timber.d("fetching movies($this) at page $page")
}

fun MoviesType.logPagingResult(fetchedSize: Int, totalSize: Int) {
    Timber.d("fetched $fetchedSize movies($this) and total movies are $totalSize")
}

fun logPagingStart(page: Int) {
    Timber.d("fetching at page $page")
}

fun logPagingResult(fetchedSize: Int, totalSize: Int) {
    Timber.d("fetched $fetchedSize movies and total movies are $totalSize")
}
