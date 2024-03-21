package com.example.movierama.core.domain.network

fun interface RequestHeader {

    fun getBasicAuthentication(): String
}