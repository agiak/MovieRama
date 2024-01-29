package com.example.movierama.domain.search

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.model.storage.StoredSearchSuggestion
import com.example.movierama.storage.sharedpreferences.PreferenceManager
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun fetchSearchHistory(): List<StoredSearchSuggestion>
    suspend fun saveSearch(query: StoredSearchSuggestion)
}

private const val searchHistoryKey = "searchHistoryKey"
private const val maxSavedSuggestions = 5

class SearchRepositoryImpl(
    private val dispatchers: IDispatchers,
    private val dataSource: PreferenceManager,
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
}