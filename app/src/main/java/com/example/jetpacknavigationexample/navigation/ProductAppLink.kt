package com.example.jetpacknavigationexample.navigation

import android.content.Intent
import android.net.Uri

object ProductAppLink {
    private const val SCHEME = "https"
    private const val HOST = "lcmnavigator.example"
    private const val PRODUCT_PATH = "/product"

    val uri: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .appendPath("product")
        .build()

    fun matches(intent: Intent?): Boolean {
        val data = intent?.data ?: return false
        return intent.action == Intent.ACTION_VIEW &&
            data.scheme == SCHEME &&
            data.host == HOST &&
            data.path == PRODUCT_PATH
    }
}
