package com.example.jetpacknavigationexample

import android.content.Intent
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
import com.example.jetpacknavigationexample.ui.theme.JetpackNavigationExampleTheme
import com.example.jetpacknavigationexample.ui.welcome.WelcomeRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JetpackNavigationExampleTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    WelcomeRoute(
                        onOpenProductFlow = {
                            startActivity(Intent(this@WelcomeActivity, ProductActivity::class.java))
                        },
                        onOpenProductFlowByAppLink = {
                            val appLinkIntent = Intent(Intent.ACTION_VIEW, ProductAppLink.uri).apply {
                                setPackage(this@WelcomeActivity.packageName)
                            }
                            startActivity(appLinkIntent)
                        }
                    )
                }
            }
        }
    }
}
