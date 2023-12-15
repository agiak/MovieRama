package com.example.movierama.domain.search

import com.example.movierama.domain.dispatchers.IDispatchers
import com.example.movierama.model.storage.StoredSearchSuggestion
import com.example.movierama.storage.sharedpreferences.PreferenceManager
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun fetchSearchHistory(): List<StoredSearchSuggestion>
    suspend fun saveSearch(query: StoredSearchSuggestion)
}

const val searchHistoryKey = "searchHistoryKey"
const val maxSavedSuggestions = 5

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
            val currentList: MutableList<StoredSearchSuggestion> =
                getSavedHistory().toMutableList()
            if (currentList.size == maxSavedSuggestions) currentList.removeAt(maxSavedSuggestions - 1)
            currentList.add(query)
            dataSource.put(searchHistoryKey, currentList)
        }
    }

    private fun getSavedHistory() =
        dataSource.get<List<StoredSearchSuggestion>>(searchHistoryKey, emptyList())
            .sortedByDescending { it.time }
}