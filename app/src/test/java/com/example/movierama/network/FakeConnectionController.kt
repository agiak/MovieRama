package com.example.movierama.network

import com.example.movierama.core.domain.network.ConnectionController

class FakeConnectionController : ConnectionController {

    var fakeConnection = true
    override fun isInternetAvailable(): Boolean = fakeConnection
}