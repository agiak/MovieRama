package com.example.movierama.model

enum class MoviesType(val description: String) {
    POPULAR("Popular"),
    NOW_PLAYING("Now playing"),
    TOP_RATED("Top rated"),
    UPCOMING("Upcoming")
}

data class SelectedType(val type: MoviesType, var isSelected: Boolean = false)

fun mapMoviesTypeToSelectedTypeList(selectedType: MoviesType = MoviesType.POPULAR): List<SelectedType> =
    ArrayList<SelectedType>().apply {
        MoviesType.values().forEach { moviesType ->
            add(SelectedType(type = moviesType, isSelected = moviesType == selectedType))
        }
    }
