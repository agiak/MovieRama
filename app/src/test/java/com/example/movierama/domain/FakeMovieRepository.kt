package com.example.movierama.domain

import com.example.movierama.helpers.fake_responses.validMovieResponse
import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.helpers.parseJsonFromFile
import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse

class FakeMovieRepository: MoviesRepository {

    override suspend fun getPopularMovies(currentPage: Int): MoviesResponse {
        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
    }

    override suspend fun getNowPlayingMovies(currentPage: Int): MoviesResponse {
        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
    }

    override suspend fun getUpcomingMovies(currentPage: Int): MoviesResponse {
        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
    }

    override suspend fun getTopRatedMovies(currentPage: Int): MoviesResponse {
        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
    }

    override suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMovie(movieId: Long): MovieDetailsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieCredits(movieId: Long): CreditsResponse {
        TODO("Not yet implemented")
    }

}
