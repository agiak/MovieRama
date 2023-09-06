package com.example.movierama.model.remote

import com.example.movierama.model.remote.similar.SimilarMovieNetwork
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SimilarMovieNetworkTest {

    @Test
    fun `similar network model cast to SimilarMovie model`(){
        // Given
        val similarMovieNetwork = SimilarMovieNetwork(
            false,
            backdropPath = "",
            genreIds = emptyList(),
            id = 13L,
            originalLanguage = "",
            originalTitle = "",
            overview = "",
            popularity = 0.0,
            posterPath = "/poster_path",
            releaseDate = "",
            title = "",
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        // When
        val result = similarMovieNetwork.toUiSimilarMovie()

        // Then
        assertThat(result.id).isEqualTo(13L)
        assertThat(result.poster).isEqualTo("http://image.tmdb.org/t/p/w500/poster_path")
    }
}
