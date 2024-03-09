package com.example.common.myutils

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.common.R
import timber.log.Timber


fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun AppCompatActivity.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Enables full screen mode to a fragment. Also makes status bar transparent
 * Remember to call disable at onDestroyView of the fragment
 * */
@Suppress("DEPRECATION")
fun AppCompatActivity.enableFullScreenTheme() {
    window.apply {
        Timber.d("enable full screen was called")
        statusBarColor =
            ContextCompat.getColor(this@enableFullScreenTheme, R.color.transparent)

        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
             // TODO fix issue with status bar text color
             // setDecorFitsSystemWindows(false)
             decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         } else {
             decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         }*/
    }
}

/**
 * Disables full screen to a fragment. Note that set status bar color to status_bar_color.
 * Remember to handle status bar text color according to status bar color.
 * */
@Suppress("DEPRECATION")
fun AppCompatActivity.disableFullScreenTheme() {
    window.apply {
        Timber.d("disable full screen was called")
        statusBarColor =
            ContextCompat.getColor(this@disableFullScreenTheme, R.color.status_bar_color)

        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              // TODO fix issue with status bar text color
              // setDecorFitsSystemWindows(true)
              decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
          } else {
              //clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
              decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
          }*/
    }
}

/**
 * Enables full screen mode to a fragment. Also makes status bar transparent
 * Remember to call disable at onDestroyView of the fragment
 * */
@Suppress("DEPRECATION")
fun Fragment.enableFullScreenTheme() {
    requireActivity().window.apply {
        Timber.d("enable full screen was called")
        statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.transparent)

        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // TODO fix issue with status bar text color
            // setDecorFitsSystemWindows(false)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }*/
    }
}

/**
 * Disables full screen to a fragment. Note that set status bar color to status_bar_color.
 * Remember to handle status bar text color according to status bar color.
 * */
@Suppress("DEPRECATION")
fun Fragment.disableFullScreenTheme() {
    requireActivity().window.apply {
        Timber.d("disable full screen was called")
        statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_color)

        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // TODO fix issue with status bar text color
            // setDecorFitsSystemWindows(true)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        } else {
            //clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
        }*/
    }
}

/**
 * Set whether the status bar has light color or not. In case status bar has light color the text color
 * will be black otherwise white.
 * */
fun Fragment.setLightStatusBars(isLightStatusBar: Boolean) {
    WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView)
        .isAppearanceLightStatusBars = isLightStatusBar
}

/**
 * Extension function to hide the soft keyboard.
 *
 * @param view The [View] that currently has focus and is showing the keyboard.
 */
fun View.hideKeyboard() {
    // Create an InputMethodManager instance
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Hide the soft keyboard for the specified view
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Extension function to open the soft keyboard for a given view.
 *
 */
fun View.showKeyboard() {
    // Create an InputMethodManager instance
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Show the soft keyboard for the specified view
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.isPermissionGranted(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED
