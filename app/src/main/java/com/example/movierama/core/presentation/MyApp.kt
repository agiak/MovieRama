package com.example.movierama.core.presentation

import android.app.Application
import com.example.movierama.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree


@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
