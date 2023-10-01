package com.example.movierama.domain.settings

import com.example.movierama.model.settings.Language
import com.example.movierama.storage.sharedpreferences.PreferenceManager
import com.example.movierama.ui.selectedLanguage
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    fun savePreferredLanguage(language: Language) {
        preferenceManager.put(selectedLanguage, language)
    }

    fun getPreferredLanguage() = preferenceManager.get(selectedLanguage, Language.English)
}