package com.example.movierama.core.data.movies

enum class MoviesType(val description: String) {
    POPULAR("Popular"),
    NOW_PLAYING("Now playing"),
    TOP_RATED("Top rated"),
    UPCOMING("Upcoming")
}


fun MoviesType.getHomePosition(): Int =
    when(this){
        MoviesType.POPULAR -> 0
        MoviesType.NOW_PLAYING -> 3
        MoviesType.TOP_RATED -> 2
        MoviesType.UPCOMING -> 1
    }