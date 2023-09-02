package com.example.myutils

import java.util.Locale

fun Float.roundToTwoDecimal(): Float =
    String.format(Locale.getDefault(), "%.2f", this).toFloat()
