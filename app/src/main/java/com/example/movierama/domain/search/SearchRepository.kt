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

class SearchRepositoryImpl(
    private val dispatchers: IDispatchers,
    private val dataSource: PreferenceManager,
) : SearchRepository {
    override suspend fun fetchSearchHistory(): List<StoredSearchSuggestion> =
        withContext(dispatchers.backgroundThread()) {
            dataSource.get(searchHistoryKey, emptyList())
        }

    override suspend fun saveSearch(query: StoredSearchSuggestion) {
        withContext(dispatchers.backgroundThread()) {
            val currentList: MutableList<StoredSearchSuggestion> =
                dataSource.get(searchHistoryKey, mutableListOf())
            if (currentList.size == 5) currentList.removeAt(4)
            currentList.add(query)
            dataSource.put(searchHistoryKey, currentList)
        }
    }
}