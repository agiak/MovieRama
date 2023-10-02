package com.example.movierama.network

class FakeConnectionController : ConnectionController {

    var fakeConnection = true
    override fun isInternetAvailable(): Boolean = fakeConnection
}