package com.example.movierama.storage

import com.example.movierama.core.domain.storage.sharedpreferences.PreferenceManager

class FakePreferenceManager<G>: PreferenceManager {

    companion object {
        const val FAVOURITE_MOVIES_KEY: String = "favourite_movies_key"
    }

    val fakeMap: MutableMap<String, G> = mutableMapOf()

    override fun <T> get(key: String, defaultValue: T): T {
        return (fakeMap[key] ?: defaultValue) as T
    }

    override fun <T> put(key: String, value: T) {
        fakeMap[key] = value as G
    }

    override fun delete(key: String) {
        fakeMap.remove(key)
    }

    override fun deleteAll() {
        fakeMap.clear()
    }

    override fun contains(key: String): Boolean = fakeMap.contains(key)

}
