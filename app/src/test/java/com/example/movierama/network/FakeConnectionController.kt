package com.example.movierama.network

import com.example.movierama.network.domain.ConnectionController

class FakeConnectionController : ConnectionController {

    var fakeConnection = true
    override fun isInternetAvailable(): Boolean = fakeConnection
}