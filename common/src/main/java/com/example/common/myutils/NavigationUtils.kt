package com.example.common.myutils

import androidx.navigation.NavController
import timber.log.Timber

fun NavController.printBackstackQueue() {
    backQueue.forEachIndexed { index, navBackStackEntry ->
        if (index != 0) Timber.d("${navBackStackEntry.destination.label}")
    }
}

fun NavController.addPrintingBackstack() {
    addOnDestinationChangedListener { _, _, _ ->
        Timber.d("--------------------------------------")
        printBackstackQueue()
    }
}