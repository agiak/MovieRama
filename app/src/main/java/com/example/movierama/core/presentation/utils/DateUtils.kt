package com.example.movierama.core.presentation.utils

import com.example.movierama.core.domain.date_input_format
import com.example.movierama.core.domain.date_output_format
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Format a string to the desired date format.
 *
 * 2023-01-20 -> 20 January 23
 * */
fun String.mapToDate(): String {
    if (isEmpty()) return ""
    val inputFormat = DateTimeFormatter.ofPattern(date_input_format, Locale.getDefault())
    val outputFormat = DateTimeFormatter.ofPattern(date_output_format, Locale.getDefault())

    val date = LocalDate.parse(this, inputFormat)
    return date.format(outputFormat)
}
