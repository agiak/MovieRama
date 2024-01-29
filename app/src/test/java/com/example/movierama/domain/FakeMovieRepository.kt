package com.example.movierama.domain

import com.example.movierama.domain.movies.MoviesRepository
import com.example.movierama.helpers.fake_responses.validMovieResponse
import com.example.movierama.helpers.parseJsonFromFile
import com.example.movierama.model.remote.credits.CreditsResponse
import com.example.movierama.model.remote.movies.MovieDetailsResponse
import com.example.movierama.model.remote.movies.MoviesResponse
import com.example.movierama.model.remote.reviews.ReviewsResponse
import com.example.movierama.model.remote.similar.SimilarResponse
import com.example.movierama.model.storage.StoredFavouriteMovie
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository {

//    override suspend fun getPopularMovies(currentPage: Int): MoviesResponse {
//        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
//    }
//
//    override suspend fun getNowPlayingMovies(currentPage: Int): MoviesResponse {
//        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
//    }
//
//    override suspend fun getUpcomingMovies(currentPage: Int): MoviesResponse {
//        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
//    }
//
//    override suspend fun getTopRatedMovies(currentPage: Int): MoviesResponse {
//        return parseJsonFromFile<MoviesResponse>(validMovieResponse)!!
//    }
//
//    override suspend fun searchMovies(
//        page: Int,
//        movieName: String?,
//        year: String?
//    ): MoviesResponse {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getMovie(movieId: Long): MovieDetailsResponse {
//        return MovieDetailsResponse()
//    }
//
//    override suspend fun getReviews(movieId: Long, currentPage: Int): ReviewsResponse {
//        return ReviewsResponse(emptyList(),0,0,0)
//    }
//
//    override suspend fun getSimilarMovies(movieId: Long, currentPage: Int): SimilarResponse {
//        return SimilarResponse(1, emptyList(),0,0)
//    }
//
//    override suspend fun getMovieCredits(movieId: Long): CreditsResponse {
//        return CreditsResponse(emptyList(), emptyList(), 2)
//    }
//
//    override suspend fun onFavouriteStatusChanged(movie: StoredFavouriteMovie) {
//
//    }
//
//    override suspend fun isMovieFavourite(movieId: Long): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun fetchFavouriteMovies(): Flow<List<StoredFavouriteMovie>> {
//        TODO("Not yet implemented")
//    }

}
