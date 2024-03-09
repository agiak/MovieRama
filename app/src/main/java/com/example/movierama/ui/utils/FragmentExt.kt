package com.example.movierama.ui.utils

import androidx.fragment.app.Fragment
import com.example.common.myutils.showDialog
import com.example.movierama.R

fun Fragment.showConnectionErrorDialog(retryAction: () -> Unit = {}) {
    showDialog(
        context = requireContext(),
        title = getString(R.string.error_network),
        drawableId = R.drawable.ic_no_conection,
        message = getString(R.string.error_network_description),
        optionalButton = getString(R.string.dialog_btn_try_again),
        mandatoryButton = getString(R.string.dialog_btn_cancel),
        optionalAction = retryAction
    )
}