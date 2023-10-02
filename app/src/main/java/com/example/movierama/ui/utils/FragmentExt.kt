package com.example.movierama.ui.utils

import androidx.fragment.app.Fragment
import com.example.movierama.R
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.network.interceptors.AuthFailedException
import com.example.movierama.network.interceptors.DeserializationException
import com.example.movierama.network.interceptors.NoInternetException
import com.example.movierama.network.interceptors.ServerException
import com.example.movierama.network.interceptors.ServerNotFoundException
import com.example.myutils.showDialog
import com.example.myutils.showToast

fun Fragment.showConnectionErrorDialog(retryAction: () -> Unit = {}) {
    showDialog(
        context = requireContext(),
        title = getString(R.string.error_network),
        drawableId = R.drawable.ic_no_conection,
        message = getString(R.string.error_network_description),
        mandatoryButton = getString(R.string.dialog_btn_try_again),
        optionalButton = getString(R.string.dialog_btn_cancel),
        mandatoryAction = retryAction
    )
}

fun Fragment.handleApiError(exception: Exception, retryAction: () -> Unit = {}): ApiError {

    return when (exception) {
        is NoInternetException -> {
            showConnectionErrorDialog(retryAction)
            ApiError.NoInternetConnection
        }

        is DeserializationException -> {
            showToast(getString(R.string.error_generic))
            ApiError.Deserialization
        }

        is AuthFailedException -> {
            showToast(getString(R.string.error_authorization))
            ApiError.Authorization
        }

        is ServerException -> {
            showToast(getString(R.string.error_server))
            ApiError.Server
        }

        is ServerNotFoundException -> {
            showToast(getString(R.string.error_server_not_found))
            ApiError.ServerNotFound
        }

        else -> {
            showToast(getString(R.string.error_generic))
            ApiError.Generic
        }
    }
}
