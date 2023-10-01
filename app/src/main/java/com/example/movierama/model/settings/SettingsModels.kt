package com.example.movierama.model.settings

import com.example.movierama.R
import com.example.movierama.ui.features.settings.SettingItem

enum class Language(val displayedNameId: Int, val code: String) {
    Greek(R.string.settings_language_greek, "el"),
    English(R.string.settings_language_english, "en")
}

fun Language.mapToSettingItem() = SettingItem.SettingItemLanguage(item = this)

enum class Orientation(val displayedNameId: Int, val code: Int) {
    Portrait(R.string.settings_landscape_portrait, 1),
    Landscape(R.string.settings_landscape_landscape, 2)
}

fun Orientation.mapToSettingItem() = SettingItem.SettingItemOrientation(item = this)

enum class Theme(val displayedNameId: Int, val code: String) {
    Dark(R.string.settings_dark_theme, "Dark"),
    Light(R.string.settings_light_theme, "Light")
}

fun Theme.mapToSettingItem() = SettingItem.SettingItemTheme(item = this)