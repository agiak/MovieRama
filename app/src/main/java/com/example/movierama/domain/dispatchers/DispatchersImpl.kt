package com.example.movierama.domain.dispatchers

import kotlinx.coroutines.Dispatchers


/**
 * Implementation of the IDispatchers interface that provides various dispatchers for different threads.
 */
class DispatchersImpl : IDispatchers {

    /**
     * Returns the dispatcher for the main thread.
     *
     * @return The dispatcher for the main thread.
     */
    override fun mainThread() = Dispatchers.Main

    /**
     * Returns the dispatcher for the background thread.
     *
     * @return The dispatcher for the background thread.
     */
    override fun backgroundThread() = Dispatchers.IO

    /**
     * Returns the dispatcher for the default thread.
     *
     * @return The dispatcher for the default thread.
     */
    override fun defaultThread() = Dispatchers.Default

}
