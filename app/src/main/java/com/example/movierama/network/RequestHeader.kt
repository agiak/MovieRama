package com.example.movierama.network

fun interface RequestHeader {

    fun getBasicAuthentication(): String
}