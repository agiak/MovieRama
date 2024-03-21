package com.example.movierama.features.search.data

data class SearchSuggestion(
    val query: String,
    val date: String,
)

data class SearchedMovie(
    val id: Long,
    val title: String,
    val logo: String,
)
