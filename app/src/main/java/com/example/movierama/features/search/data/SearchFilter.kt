package com.example.movierama.features.search.data

data class SearchFilter(
    val movieName: String? = null,
    val year: String? = null,
) {
    fun isEmpty() = movieName.isNullOrBlank() && year.isNullOrBlank()

    val value: String
        get() = movieName ?: year ?: ""
}
