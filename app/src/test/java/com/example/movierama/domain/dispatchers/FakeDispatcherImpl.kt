package com.example.movierama.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class FakeDispatcherImpl: IDispatchers {

    override fun mainThread(): CoroutineDispatcher = UnconfinedTestDispatcher()

    override fun backgroundThread(): CoroutineDispatcher = UnconfinedTestDispatcher()

    override fun defaultThread(): CoroutineDispatcher = UnconfinedTestDispatcher()

}
