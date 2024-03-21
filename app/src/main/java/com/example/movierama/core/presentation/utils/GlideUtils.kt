package com.example.movierama.core.presentation.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

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

fun ImageView.loadRoundedCorners(url: String, placeholder: Int? = null, error: Int? = null, roundness: Int){
    Glide.with(this.context)
        .load(url)
        .apply {
            placeholder?.let(::placeholder)
            error?.let(::error)
        }
        .apply(RequestOptions.bitmapTransform(RoundedCorners(roundness)))
        .into(this)
}
