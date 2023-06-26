package com.example.movierama.ui.utils

import com.example.movierama.domain.dispatchers.IDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Utility class for debouncing actions.
 *
 * @param dispatcher The dispatcher used for launching coroutines.
 * @param debounceDelay The delay in milliseconds for the debounce mechanism. Default value is 600 milliseconds.
 */
class DebounceUtil @Inject constructor(
    private val dispatcher: IDispatchers,
    private val debounceDelay: Long = 600L
) {
    private var searchJob: Job? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher.backgroundThread())

    /**
     * Debounces the given suspend action.
     *
     * @param action The suspend action to be debounced.
     */
    fun debounce(
        action: suspend () -> Unit
    ) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(debounceDelay)
            action()
        }
    }
}
