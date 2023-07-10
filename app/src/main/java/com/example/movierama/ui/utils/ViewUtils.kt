package com.example.movierama.ui.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

// Set the top margin of a View
fun View.setMarginTop(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = margin
    }
}

// Set the bottom margin of a View
fun View.setMarginBottom(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        bottomMargin = margin
    }
}

// Set the left margin of a View
fun View.setMarginLeft(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = margin
    }
}

// Set the right margin of a View
fun View.setMarginRight(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        rightMargin = margin
    }
}

// Set the vertical margins (top and bottom) of a View
fun View.setMarginVertical(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = margin
        bottomMargin = margin
    }
}

// Set the horizontal margins (left and right) of a View
fun View.setMarginHorizontal(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = margin
        rightMargin = margin
    }
}