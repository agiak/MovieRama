package com.example.common.myutils

import android.app.DatePickerDialog
import android.widget.EditText
import com.example.common.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun EditText.setCursorPositionToEnd() = setSelection(text.toString().length)

fun EditText.onDateListener(format: String= "yyyy/MM/dd") {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    setOnClickListener {
        DatePickerDialog(context, R.style.TimePickerTheme,
            { _, year, month, day ->
                this.setText(getFormattedTime(year, month, day, format))
            }, year, month, day).show()
    }
}

// format time from date picker in birthdate field
private fun getFormattedTime(year: Int, month: Int, dayOfMonth: Int, format: String): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    val dateFormatter = SimpleDateFormat(format, Locale.ENGLISH)
    return dateFormatter.format(calendar.time)
}