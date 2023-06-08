package com.example.movierama.domain.movies

import android.util.Log
import com.example.movierama.data.network.movies.MovieNetwork
import com.example.movierama.data.network.movies.MoviesResponse
import com.example.movierama.data.network.MoviesService
import com.example.movierama.data.network.movies.MovieDetailsResponse
import com.example.movierama.data.network.reviews.AuthorDetails
import com.example.movierama.data.network.reviews.ReviewNetwork
import com.example.movierama.data.network.reviews.ReviewsResponse
import com.example.movierama.data.network.similar.SimilarMovieNetwork
import com.example.movierama.data.network.similar.SimilarResponse
import com.example.movierama.domain.dispatchers.IDispatchers
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class MoviesRepository @Inject constructor(
    private val dispatchersImpl: IDispatchers,
    private val service: MoviesService,
) {

    private val dummyProvider: DummyProvider = DummyProvider()

    suspend fun getMovies(
        currentPage: Int
    ): MoviesResponse = withContext(dispatchersImpl.backgroundThread()) {
        Log.d("MoviesRepository", "Calling movies service with page $currentPage")
        delay(2000)
        //service.getMovies(page = currentPage)
        MoviesResponse(
            page = currentPage,
            moviesNetwork = dummyProvider.getDummyMovies(),
            totalPages = 1,
            totalResults = 20
        )
    }

    private fun getDummyMovies() =
        listOf(
            MovieNetwork(
                adult = false,
                backdrop_path = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
                genre_ids = listOf(12, 16, 28),
                id = 123456,
                original_language = "en",
                original_title = "Example Movie 1",
                overview = "This is an example movie 1.",
                popularity = 7.8,
                poster_path = "/example_poster1.jpg",
                release_date = "2023-01-01",
                title = "Example Movie 1",
                video = false,
                vote_average = 7.2F,
                vote_count = 123
            ),
            MovieNetwork(
                adult = false,
                backdrop_path = "/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
                genre_ids = listOf(18, 36),
                id = 789012,
                original_language = "en",
                original_title = "Example Movie 2",
                overview = "This is an example movie 2.",
                popularity = 8.5,
                poster_path = "/example_poster2.jpg",
                release_date = "2023-02-01",
                title = "Example Movie 2",
                video = false,
                vote_average = 8.0F,
                vote_count = 456
            ),
            MovieNetwork(
                adult = false,
                backdrop_path = "/nDxJJyA5giRhXx96q1sWbOUjMBI.jpg",
                genre_ids = listOf(80, 99),
                id = 345678,
                original_language = "en",
                original_title = "Example Movie 3",
                overview = "This is an example movie 3.",
                popularity = 6.9,
                poster_path = "/example_poster3.jpg",
                release_date = "2023-03-01",
                title = "Example Movie 3",
                video = false,
                vote_average = 6.5F,
                vote_count = 789
            ),
            MovieNetwork(
                adult = false,
                backdrop_path = "/example_backdrop4.jpg",
                genre_ids = listOf(14, 27),
                id = 901234,
                original_language = "en",
                original_title = "Example Movie 4",
                overview = "This is an example movie 4.",
                popularity = 5.5,
                poster_path = "/example_poster4.jpg",
                release_date = "2023-04-01",
                title = "Example Movie 4",
                video = false,
                vote_average = 5.0F,
                vote_count = 567
            ),
            MovieNetwork(
                adult = false,
                backdrop_path = "/example_backdrop5.jpg",
                genre_ids = listOf(53, 80),
                id = 567890,
                original_language = "en",
                original_title = "Example Movie 5",
                overview = "This is an example movie 5.",
                popularity = 7.2,
                poster_path = "/example_poster5.jpg",
                release_date = "2023-05-01",
                title = "Example Movie 5",
                video = false,
                vote_average = 6.8F,
                vote_count = 901
            )
        )

    suspend fun getMovie(
        movieId: Long
    ): MovieDetailsResponse = withContext(dispatchersImpl.backgroundThread()) {
        delay(2000)
        //service.getMovieDetails(movieId = movieId)
        getDummyMovieDetails()
    }

    private fun getDummyMovieDetails(): MovieDetailsResponse{
        val gson = Gson()
        val jsonString = """{
            "adult": false,
            "backdrop_path": "/hZkgoQYus5vegHoetLkCJzb17zJ.jpg",
            "belongs_to_collection": null,
            "budget": 63000000,
            "genres": [
            {
                "id": 18,
                "name": "Drama"
            },
            {
                "id": 53,
                "name": "Thriller"
            },
            {
                "id": 35,
                "name": "Comedy"
            }
            ],
            "homepage": "http://www.foxmovies.com/movies/fight-club",
            "id": 550,
            "imdb_id": "tt0137523",
            "original_language": "en",
            "original_title": "Fight Club",
            "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
            "popularity": 61.416,
            "poster_path": "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            "production_companies": [
            {
                "id": 508,
                "logo_path": "/7cxRWzi4LsVm4Utfpr1hfARNurT.png",
                "name": "Regency Enterprises",
                "origin_country": "US"
            },
            {
                "id": 711,
                "logo_path": "/tEiIH5QesdheJmDAqQwvtN60727.png",
                "name": "Fox 2000 Pictures",
                "origin_country": "US"
            },
            {
                "id": 20555,
                "logo_path": "/hD8yEGUBlHOcfHYbujp71vD8gZp.png",
                "name": "Taurus Film",
                "origin_country": "DE"
            },
            {
                "id": 54051,
                "logo_path": null,
                "name": "Atman Entertainment",
                "origin_country": ""
            },
            {
                "id": 54052,
                "logo_path": null,
                "name": "Knickerbocker Films",
                "origin_country": "US"
            },
            {
                "id": 4700,
                "logo_path": "/A32wmjrs9Psf4zw0uaixF0GXfxq.png",
                "name": "The Linson Company",
                "origin_country": "US"
            },
            {
                "id": 25,
                "logo_path": "/qZCc1lty5FzX30aOCVRBLzaVmcp.png",
                "name": "20th Century Fox",
                "origin_country": "US"
            }
            ],
            "production_countries": [
            {
                "iso_3166_1": "US",
                "name": "United States of America"
            }
            ],
            "release_date": "1999-10-15",
            "revenue": 100853753,
            "runtime": 139,
            "spoken_languages": [
            {
                "english_name": "English",
                "iso_639_1": "en",
                "name": "English"
            }
            ],
            "status": "Released",
            "tagline": "Mischief. Mayhem. Soap.",
            "title": "Fight Club",
            "video": false,
            "vote_average": 8.433,
            "vote_count": 26280
        }"""

        return gson.fromJson(jsonString, MovieDetailsResponse::class.java)

    }

    suspend fun getReviews(
        movieId: Long,
        currentPage: Int
    ): ReviewsResponse = withContext(dispatchersImpl.backgroundThread()) {
        delay(2000)
        //service.getReviews(movieId = movieId, page = currentPage)
        ReviewsResponse(
            page = currentPage,
            reviewNetworks = getDummyReviews(),
            totalPages = 1,
            totalResults = 20
        )
    }

    private fun getDummyReviews() =
        listOf(
            ReviewNetwork(
                id = Random.nextLong(),
                author = "Dummy Author 1",
                authorDetails = AuthorDetails(
                    avatarPath = "dummy_avatar_path_1",
                    name = "Dummy Author 1",
                    rating = Random.nextInt(1, 6),
                    username = "dummy_author_1"
                ),
                content = "This is a dummy review 1",
                createdAt = "2023-06-07T12:00:00Z",
                updatedAt = "2023-06-07T12:00:00Z",
                url = "https://dummy-review-url-1.com"
            ),
            ReviewNetwork(
                id = Random.nextLong(),
                author = "Dummy Author 2",
                authorDetails = AuthorDetails(
                    avatarPath = "dummy_avatar_path_2",
                    name = "Dummy Author 2",
                    rating = Random.nextInt(1, 6),
                    username = "dummy_author_2"
                ),
                content = "This is a dummy review 2",
                createdAt = "2023-06-07T12:00:00Z",
                updatedAt = "2023-06-07T12:00:00Z",
                url = "https://dummy-review-url-2.com"
            ),
            // Repeat the above pattern for the remaining objects (3 to 20)
            ReviewNetwork(
                id = Random.nextLong(),
                author = "Dummy Author 20",
                authorDetails = AuthorDetails(
                    avatarPath = "dummy_avatar_path_20",
                    name = "Dummy Author 20",
                    rating = Random.nextInt(1, 6),
                    username = "dummy_author_20"
                ),
                content = "This is a dummy review 20",
                createdAt = "2023-06-07T12:00:00Z",
                updatedAt = "2023-06-07T12:00:00Z",
                url = "https://dummy-review-url-20.com"
            )
        )

    suspend fun getSimilarMovies(
        movieId: Long,
        currentPage: Int
    ): SimilarResponse = withContext(dispatchersImpl.backgroundThread()) {
        delay(2000)
        //service.getSimilarMovies(movieId = movieId, page = currentPage)
        SimilarResponse(
            page = currentPage,
            similarMovieNetworks = getDummySimilarMovies(),
            totalPages = 1,
            totalResults = 20
        )
    }

    private fun getDummySimilarMovies() = listOf(
        SimilarMovieNetwork(
            adult = true,
            backdropPath = "/dummy_backdrop_path_1.jpg",
            genreIds = listOf(1, 2, 3),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Dummy Movie 1",
            overview = "This is a dummy movie 1",
            popularity = 7.8,
            posterPath = "/dummy_poster_path_1.jpg",
            releaseDate = "2023-06-07",
            title = "Dummy Movie 1",
            video = false,
            voteAverage = 6.5,
            voteCount = 100
        ),
        SimilarMovieNetwork(
            adult = false,
            backdropPath = "/dummy_backdrop_path_2.jpg",
            genreIds = listOf(4, 5),
            id = 2,
            originalLanguage = "en",
            originalTitle = "Dummy Movie 2",
            overview = "This is a dummy movie 2",
            popularity = 6.2,
            posterPath = "/dummy_poster_path_2.jpg",
            releaseDate = "2023-06-08",
            title = "Dummy Movie 2",
            video = true,
            voteAverage = 7.1,
            voteCount = 200
        ),
        // Repeat the above pattern for the remaining objects (3 to 20)
        SimilarMovieNetwork(
            adult = true,
            backdropPath = "/dummy_backdrop_path_20.jpg",
            genreIds = listOf(9, 10),
            id = 20,
            originalLanguage = "en",
            originalTitle = "Dummy Movie 20",
            overview = "This is a dummy movie 20",
            popularity = 8.3,
            posterPath = "/dummy_poster_path_20.jpg",
            releaseDate = "2023-06-27",
            title = "Dummy Movie 20",
            video = false,
            voteAverage = 7.9,
            voteCount = 500
        )
    )
}
