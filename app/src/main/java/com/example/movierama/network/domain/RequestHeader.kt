package com.example.movierama.network.domain

fun interface RequestHeader {

    fun getBasicAuthentication(): String
}
