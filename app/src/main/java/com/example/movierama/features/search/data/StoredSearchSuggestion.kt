package com.example.movierama.features.search.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class StoredSearchSuggestion(
    @PrimaryKey val time: Long,
    val searchInput: String,
) {
    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is StoredSearchSuggestion -> false
            else -> searchInput == other.searchInput
        }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + searchInput.hashCode()
        return result
    }

    fun isEmpty() = searchInput.isEmpty()
}