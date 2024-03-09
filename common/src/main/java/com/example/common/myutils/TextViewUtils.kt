package com.example.common.myutils

import android.widget.TextView

fun TextView.setEndDrawable(drawableId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawableId)
}