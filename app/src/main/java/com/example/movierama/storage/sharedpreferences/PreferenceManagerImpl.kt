package com.example.movierama.storage.sharedpreferences

import android.content.Context
import com.orhanobut.hawk.Hawk
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(context: Context): PreferenceManager {

    companion object {
        const val FAVOURITE_MOVIES_KEY = "favourite_movies_key"
    }

    init {
        // Initialize Hawk with the provided context
        Hawk.init(context).build()
    }

    override fun <T> get(key: String, defaultValue: T): T {
        return Hawk.get(key, defaultValue)
    }

    override fun <T> put(key: String, value: T) {
        Hawk.put(key, value)
    }

    override fun delete(key: String) {
        Hawk.delete(key)
    }

    override fun deleteAll() {
        Hawk.deleteAll()
    }

    override fun contains(key: String): Boolean {
        return Hawk.contains(key)
    }
}
