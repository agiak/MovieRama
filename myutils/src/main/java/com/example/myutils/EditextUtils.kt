package com.example.myutils

import android.widget.EditText

fun EditText.setCursorPositionToEnd() = setSelection(text.toString().length)