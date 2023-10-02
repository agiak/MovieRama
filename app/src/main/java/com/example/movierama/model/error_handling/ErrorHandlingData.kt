package com.example.movierama.model.error_handling

import com.example.movierama.R

enum class ApiError(val messageId: Int, val drawableId: Int) {
    NoInternetConnection(R.string.error_network, R.drawable.ic_no_conection),
    Deserialization(R.string.error_deserialization, R.drawable.ic_deserialization_error),
    Authorization(R.string.error_authorization, R.drawable.ic_authariztion_error),
    Server(R.string.error_server, R.drawable.ic_server_error),
    ServerNotFound(R.string.error_server_not_found, R.drawable.ic_server_not_found_error),
    Generic(R.string.error_generic, R.drawable.ic_generic_error)
}