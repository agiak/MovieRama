package com.example.movierama.ui.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(url: String, placeholder: Int? = null, error: Int? = null) {
    Glide.with(context)
        .load(url)
        .apply {
            placeholder?.let(::placeholder)
            error?.let(::error)
        }
        .into(this)
}

fun ImageView.loadCircle(url: String, placeholder: Int? = null, error: Int? = null){
    Glide.with(context)
        .load(url)
        .circleCrop()
        .apply {
            placeholder?.let(::placeholder)
            error?.let(::error)
        }
        .into(this)
}
