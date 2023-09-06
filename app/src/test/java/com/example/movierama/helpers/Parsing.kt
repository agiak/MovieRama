package com.example.movierama.helpers

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.File
import java.io.FileReader

inline fun <reified T> parseJson(jsonString: String): T? {
    val gson = Gson()
    try {
        return gson.fromJson(jsonString, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

inline fun <reified T> parseJsonFromFile(file: File): T? {
    val gson = Gson()
    try {
        val jsonReader = FileReader(file)
        val typeToken = object : TypeToken<T>() {}.type
        return gson.fromJson(jsonReader, typeToken)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

