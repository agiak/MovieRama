package com.example.movierama.core.domain.errorhadling

fun interface ErrorHandler {

    fun getErrorMessage(exception: Exception): String

}
