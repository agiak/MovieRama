package com.example.movierama.network.domain.interceptors

import com.example.movierama.network.domain.ConnectionController
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ErrorHandlerInterceptor @Inject constructor(
    private val connectionController: ConnectionController
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        connectionController.takeIf { it.isInternetAvailable() }
            ?: throw NoInternetException("No internet connection")

        val request = chain.request()
        val response = chain.proceed(request)
        when {
            response.code.isAuthorizationErrorCode() -> throw AuthFailedException("Authorization failed")
            response.code.isServerErrorCode() -> throw ServerException("Server failed")
            response.code.isServerNotFoundErrorCode() -> throw ServerNotFoundException("Server not found")
            !response.isSuccessful -> throw DeserializationException("Error parsing response")
        }
        return response
    }

    private fun Int.isAuthorizationErrorCode() = this == 401 || this == 403
    private fun Int.isServerErrorCode() = this >= 500
    private fun Int.isServerNotFoundErrorCode() = this == 404
}

data class NoInternetException(override val message: String) : IOException(message)
data class DeserializationException(override val message: String) : IOException(message)
data class AuthFailedException(override val message: String) : IOException(message)
data class ServerException(override val message: String) : IOException(message)
data class ServerNotFoundException(override val message: String) : IOException(message)
