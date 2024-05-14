package com.example.movierama.core.data.errorhandling

import android.content.Context
import androidx.annotation.StringRes

sealed class UiMessage {
    data class Dynamic(val message: String): UiMessage()
    data class Resource(@StringRes val messageId: Int): UiMessage()

    fun asError(context: Context): String =
        when(this) {
            is Dynamic -> message
            is Resource -> context.getString(messageId)
        }
}
