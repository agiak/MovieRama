package com.example.movierama.core.domain.error_hadling

fun interface ErrorHandler {

    fun getErrorMessage(exception: Exception): String

}
