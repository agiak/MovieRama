package com.example.movierama.network

interface RequestHeader {

    fun getBasicAuthentication(): String
}