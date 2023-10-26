package com.example.movierama.model.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class StoredSearchSuggestion(
    @PrimaryKey val time: Long,
    val searchInput: String,
)