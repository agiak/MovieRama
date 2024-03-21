package com.example.movierama.core.presentation.utils

import com.example.movierama.core.domain.dispatchers.IDispatchers
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
const val DEFAULT_DEBOUNCE_TIME = 600

class DebounceUtil @Inject constructor(
    dispatcher: IDispatchers,
    private var debounceDelay: Int = DEFAULT_DEBOUNCE_TIME,
) {
    private var searchJob: Job? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher.mainThread())

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
            delay(debounceDelay.toLong())
            action()
        }
    }

    fun setDebounceTime(time: Int) {
        debounceDelay = time
    }
}
