package com.example.jetpacknavigationexample.navigation

import android.content.Intent

object ProductAppLink {
    private const val SCHEME = "https"
    private const val HOST = "lcmnavigator.example"
    private const val PRODUCT_PATH = "/product"

    fun matches(intent: Intent?): Boolean {
        val data = intent?.data ?: return false
        return intent.action == Intent.ACTION_VIEW &&
            data.scheme == SCHEME &&
            data.host == HOST &&
            data.path == PRODUCT_PATH
    }
}
