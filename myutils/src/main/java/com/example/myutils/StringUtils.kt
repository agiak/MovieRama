package com.example.myutils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(): Date? {
    return try {
        SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(this)
    }catch (e: Exception){
        Log.e("DateParsing",e.message ?: "General Exception")
        null
    }
}

fun String.isNumber(): Boolean = toIntOrNull() != null
