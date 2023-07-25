package com.example.movierama.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FakeDispatcherImpl: IDispatchers {

    override fun mainThread(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun backgroundThread(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun defaultThread(): CoroutineDispatcher = Dispatchers.Unconfined

}
