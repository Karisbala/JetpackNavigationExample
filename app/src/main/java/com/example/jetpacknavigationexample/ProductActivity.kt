package com.example.jetpacknavigationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.example.jetpacknavigationexample.navigation.ProductAppLink
import com.example.jetpacknavigationexample.ui.navigation.ProductNavHost
import com.example.jetpacknavigationexample.ui.theme.JetpackNavigationExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val openProductByAppLinkInitially = savedInstanceState == null && ProductAppLink.matches(intent)

        setContent {
            JetpackNavigationExampleTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    ProductNavHost(
                        openProductByAppLinkInitially = openProductByAppLinkInitially,
                        onExitRequested = {
                            finish()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
