package com.example.common.myutils

import kotlin.math.pow

fun Float.roundToTwoDecimal(decimals: Int = 2): Float {
    val factor = 10.0.pow(decimals).toFloat()
    return (this * factor).toInt() / factor
}
