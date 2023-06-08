package com.example.movierama.ui.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String, duration: Int= Toast.LENGTH_LONG){
    Toast.makeText(requireContext(), message, duration).show()
}

fun AppCompatActivity.showToast(message: String, duration: Int= Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}