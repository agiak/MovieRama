package com.example.movierama.domain.error_hadling

interface ErrorHandler {

    fun getErrorMessage(exception: Exception): String

}
