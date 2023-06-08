package com.example.movierama.domain.dispatchers

import kotlinx.coroutines.Dispatchers


class DispatchersImpl : IDispatchers {

    override fun mainThread() = Dispatchers.Main

    override fun backgroundThread() = Dispatchers.IO

    override fun defaultThread()  = Dispatchers.Default

}