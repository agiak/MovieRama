package com.example.movierama.core.domain.storage.sharedpreferences

interface PreferenceManager {

    /**
     * Retrieves the value associated with the given key from preferences.
     *
     * @param key The key used to retrieve the value.
     * @param defaultValue The default value to be returned if the key is not found.
     * @return The value associated with the key, or the defaultValue if not found.
     */
    fun <T> get(key: String, defaultValue: T): T

    /**
     * Stores the given value associated with the provided key in preferences.
     *
     * @param key The key used to store the value.
     * @param value The value to be stored.
     */
    fun <T> put(key: String, value: T)

    /**
     * Deletes the preference entry associated with the given key.
     *
     * @param key The key of the preference entry to be deleted.
     */
    fun delete(key: String)

    /**
     * Deletes all preference entries.
     */
    fun deleteAll()

    /**
     * Checks if a preference entry exists with the given key.
     *
     * @param key The key to check for existence.
     * @return true if a preference entry with the key exists, false otherwise.
     */
    fun contains(key: String): Boolean
}
