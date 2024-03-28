package com.example.movierama.network.data

import com.example.movierama.R
import com.example.movierama.network.domain.interceptors.AuthFailedException
import com.example.movierama.network.domain.interceptors.DeserializationException
import com.example.movierama.network.domain.interceptors.NoInternetException
import com.example.movierama.network.domain.interceptors.ServerException
import com.example.movierama.network.domain.interceptors.ServerNotFoundException

enum class ApiError(val messageId: Int, val drawableId: Int) {
    NoInternetConnection(R.string.error_network, R.drawable.ic_no_conection),
    Deserialization(R.string.error_deserialization, R.drawable.ic_deserialization_error),
    Authorization(R.string.error_authorization, R.drawable.ic_authariztion_error),
    Server(R.string.error_server, R.drawable.ic_server_error),
    ServerNotFound(R.string.error_server_not_found, R.drawable.ic_server_not_found_error),
    Generic(R.string.error_generic, R.drawable.ic_generic_error)
}

fun Exception.toApiError(): ApiError =
    when (this) {
        is NoInternetException -> ApiError.NoInternetConnection
        is DeserializationException -> ApiError.Deserialization
        is AuthFailedException -> ApiError.Authorization
        is ServerException -> ApiError.Server
        is ServerNotFoundException -> ApiError.ServerNotFound
        else -> ApiError.Generic
    }
