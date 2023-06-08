package com.example.movierama.domain.movies

import com.example.movierama.data.network.movies.MovieNetwork
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DummyProvider {

    val images = listOf(
        "/nDxJJyA5giRhXx96q1sWbOUjMBI.jpg",
        "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
        "/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
        "/rzRb63TldOKdKydCvWJM8B6EkPM.jpg",
        "/m1fgGSLK0WvRpzM1AmZu38m0Tx8.jpg",
        "/tYcmm8XtzRdcT6kliCbHuWwLCwB.jpg",
        "/taYgn3RRpCGlTGdaGQvnSIOzXFy.jpg",
        "/eNJhWy7xFzR74SYaSJHqJZuroDm.jpg",
        "/qNz4l8UgTkD8rlqiKZ556pCJ9iO.jpg"
    )

    private val rating: Float
        get() = (0..5).random().toFloat()

    private val id: Long
        get() = (0..99999).random().toLong()

    private val popularity: Double
        get() = (0..10).random().toDouble()

    private val getRandomImage: String
        get() = images.random()

    private val adjectives = listOf("The", "Amazing", "Fantastic", "Incredible", "Magnificent", "Spectacular", "Wonderful")
    private val nouns = listOf("Adventure", "Journey", "Quest", "Mystery", "Enigma", "Legend", "Tale")

    val movieName: String
        get() = "${adjectives.random()} ${nouns.random()}"

    private val descriptions = listOf("A thrilling rollercoaster ride", "An epic battle of good and evil", "A heartwarming tale of love and friendship", "A mind-bending mystery", "A captivating journey through time")

    private val description: String
        get() = descriptions.random()

    private fun generateRandomDate(): String {
        val startDate = LocalDate.of(1970, 1, 1) // Change start date as desired
        val endDate = LocalDate.of(2023, 12, 31) // Change end date as desired

        val randomDate = startDate.plusDays((Math.random() * (endDate.toEpochDay() - startDate.toEpochDay() + 1)).toLong())
        return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun getDummyMovies(maxMovies: Int = 5): List<MovieNetwork> {
        return ArrayList<MovieNetwork>().apply {
            for (i in 0..maxMovies) {
                MovieNetwork(
                    adult = false,
                    backdrop_path = "/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
                    genre_ids = listOf(18, 36),
                    id = id,
                    original_language = "en",
                    original_title = movieName,
                    overview = description,
                    popularity = popularity,
                    poster_path = getRandomImage,
                    release_date = generateRandomDate(),
                    title = movieName,
                    video = false,
                    vote_average = rating,
                    vote_count = 456
                )
            }
        }
    }
}