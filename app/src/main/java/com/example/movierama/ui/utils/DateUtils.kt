package com.example.movierama.ui.utils

import com.example.movierama.ui.date_input_format
import com.example.movierama.ui.date_output_format
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.mapToDate(): String {
    val inputFormat = DateTimeFormatter.ofPattern(date_input_format, Locale.getDefault())
    val outputFormat = DateTimeFormatter.ofPattern(date_output_format, Locale.getDefault())

    val date = LocalDate.parse(this, inputFormat)
    return date.format(outputFormat)
}