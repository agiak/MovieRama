package com.example.movierama.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject

fun interface ConnectionController {
    fun isInternetAvailable(): Boolean
}

class ConnectionControllerImpl @Inject constructor(
    private val context: Context
) : ConnectionController {

    override fun isInternetAvailable(): Boolean = context.isInternetAvailable()

    @Suppress("DEPRECATION")
    private fun Context.isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }

            else -> {
                val networks = cm.allNetworkInfo
                for (nInfo in networks) {
                    if (nInfo != null && nInfo.isConnected) return true
                }
            }
        }
        return false
    }
}
