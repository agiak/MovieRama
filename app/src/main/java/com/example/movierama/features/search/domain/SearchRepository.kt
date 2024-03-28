package com.example.movierama.features.search.domain

import com.example.movierama.core.domain.dispatchers.IDispatchers
import com.example.movierama.features.search.data.StoredSearchSuggestion
import com.example.movierama.storage.domain.sharedpreferences.PreferenceManager
import com.example.movierama.core.data.movies.MoviesResponse
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun fetchSearchHistory(): List<StoredSearchSuggestion>
    suspend fun saveSearch(query: StoredSearchSuggestion)

    /**
     * Searches for movies based on the provided criteria.
     *
     * @param page The page number.
     * @param movieName The name of the movie to search for.
     * @param year The release year of the movie to search for.
     * @return The response containing the list of search results.
     */
    suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse

}

private const val searchHistoryKey = "searchHistoryKey"
private const val maxSavedSuggestions = 5

class SearchRepositoryImpl(
    private val dispatchers: IDispatchers,
    private val dataSource: PreferenceManager,
    private val service: SearchService,
) : SearchRepository {
    override suspend fun fetchSearchHistory(): List<StoredSearchSuggestion> =
        withContext(dispatchers.backgroundThread()) {
            getSavedHistory()
        }

    override suspend fun saveSearch(query: StoredSearchSuggestion) {
        withContext(dispatchers.backgroundThread()) {
            if (query.isEmpty()) return@withContext

            val previousSearches: MutableList<StoredSearchSuggestion> =
                getSavedHistory().toMutableList()
            if (previousSearches.contains(query)) return@withContext
            if (previousSearches.size == maxSavedSuggestions) previousSearches.removeAt(
                maxSavedSuggestions - 1
            )
            previousSearches.add(query)
            dataSource.put(searchHistoryKey, previousSearches)
        }
    }

    private fun getSavedHistory(): List<StoredSearchSuggestion> =
        dataSource.get<List<StoredSearchSuggestion>>(searchHistoryKey, emptyList())
            .sortedByDescending { it.time }

    override suspend fun searchMovies(
        page: Int,
        movieName: String?,
        year: String?
    ): MoviesResponse = withContext(dispatchers.backgroundThread()) {
        service.searchMovies(page = page, movieName = movieName, year = year)
    }
}
