package com.example.movierama.ui

/**
 * Represents the state of UI components.
 */
sealed class UIState<out T> {

    /**
     * Successful state.
     */
    object Success: UIState<Nothing>()

    /**
     * Successful state containing some data as well.
     */
    data class Result<out T>(val data: T): UIState<T>()

    /**
     * Failed state, containing the error as well.
     */
    data class Error(val error: Throwable): UIState<Nothing>()

    /**
     * Successful state that doesn't contain any data, when there was supposed to.
     */
    object Empty: UIState<Nothing>()

    /**
     * State about to change.
     */
    object InProgress: UIState<Nothing>()

    object LoadingMore: UIState<Nothing>()

    /**
     * State about to change.
     */
    object Refreshing: UIState<Nothing>()

    /**
     * Undefined state.
     */
    object IDLE: UIState<Nothing>()

    companion object {
        fun <T> Result<List<T>>.orEmpty() = if (data.isEmpty()) Empty else this
    }
}
