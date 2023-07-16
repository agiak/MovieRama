package com.example.movierama.domain.error_handling

import com.example.movierama.domain.error_hadling.ErrorHandler

class FakeErrorHandler: ErrorHandler {

    var exceptionErrorMessage = ""

    override fun getErrorMessage(exception: Exception): String = exceptionErrorMessage

}
