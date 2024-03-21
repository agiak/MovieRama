package com.example.movierama.core.domain.error_hadling

import android.content.Context
import com.example.movierama.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor(
    private val context: Context
): ErrorHandler {

    override fun getErrorMessage(exception: Exception) = context.getString(exception.getErrorMessageResource())
}

fun Exception.getErrorMessageResource(): Int {
    return when (this) {
        is IOException -> R.string.error_reading_response
        is HttpException -> R.string.error_network
        else -> R.string.error_generic
    }
}
