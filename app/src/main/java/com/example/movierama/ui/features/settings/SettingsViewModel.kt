package com.example.movierama.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.model.settings.Language
import com.example.movierama.model.settings.Orientation
import com.example.movierama.model.settings.Theme
import com.example.movierama.model.settings.mapToSettingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun onLanguageChanged(newLanguage: Language) {
        viewModelScope.launch {
            _settingsState.update {
                it.copy(selectedLanguage = newLanguage)
            }
        }
    }

    fun onOrientationChanged(newOrientation: Orientation) {
        viewModelScope.launch {
            _settingsState.update {
                it.copy(selectedOrientation = newOrientation)
            }
        }
    }

    fun onThemeChanged(newTheme: Theme) {
        viewModelScope.launch {
            _settingsState.update {
                it.copy(selectedTheme = newTheme)
            }
        }
    }

    fun getDefaultSettingsItem(): List<SettingItem> = listOf(
        SettingItem.SettingItemLanguage(),
        SettingItem.SettingItemOrientation(),
        SettingItem.SettingItemTheme()
    )
}

data class SettingsState(
    val selectedLanguage: Language = Language.English,
    val selectedOrientation: Orientation = Orientation.Portrait,
    val selectedTheme: Theme = Theme.Light
) {
    fun getSettingItemList(): List<SettingItem> = listOf(
        selectedLanguage.mapToSettingItem(),
        selectedOrientation.mapToSettingItem(),
        selectedTheme.mapToSettingItem()
    )
}