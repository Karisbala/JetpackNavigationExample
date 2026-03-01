package com.example.jetpacknavigationexample.navigation

import android.net.Uri

object ProductAppLink {
    private const val SCHEME = "https"
    private const val HOST = "lcmnavigator.example"

    val uri: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .appendPath("product")
        .build()
}
