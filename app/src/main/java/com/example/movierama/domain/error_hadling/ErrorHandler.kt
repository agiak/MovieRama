package com.example.movierama.domain.error_hadling

fun interface ErrorHandler {

    fun getErrorMessage(exception: Exception): String

}
