package com.example.movierama.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierama.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _showSplash = MutableStateFlow(true)
    val showSplash = _showSplash.asStateFlow()

    init {
        showSplashDuration()
    }

    private fun showSplashDuration() {
        viewModelScope.launch {
            delay(BuildConfig.SPLASH_DURATION.toLong())
            _showSplash.value = false
        }
    }
}