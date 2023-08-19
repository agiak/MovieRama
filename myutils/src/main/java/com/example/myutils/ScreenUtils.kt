package com.example.myutils

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: String, duration: Int= Toast.LENGTH_LONG){
    Toast.makeText(requireContext(), message, duration).show()
}

fun AppCompatActivity.showToast(message: String, duration: Int= Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

/**
 * Enables full screen mode to a fragment. Remember to call disable at onDestroyView of the fragment
 * */
@Suppress("DEPRECATION")
fun Fragment.enableFullScreenTheme() {
    requireActivity().window.apply {
        statusBarColor = ContextCompat.getColor(requireContext(), com.example.myresources.R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(false)
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }
}

@Suppress("DEPRECATION")
fun Fragment.disableFullScreenTheme() {
    requireActivity().window.apply {
        statusBarColor = ContextCompat.getColor(requireContext(), com.example.myresources.R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(true)
        } else {
            clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
