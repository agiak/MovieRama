package com.example.movierama.ui.utils

import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun ProgressBar.show(){
    isVisible = true
}

fun ProgressBar.hide(){
    isVisible = false
}

fun SwipeRefreshLayout.show(){
    isRefreshing = true
}

fun SwipeRefreshLayout.hide(){
    isRefreshing = false
}
