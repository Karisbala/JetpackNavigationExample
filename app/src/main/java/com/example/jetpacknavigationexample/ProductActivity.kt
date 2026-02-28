package com.example.jetpacknavigationexample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpacknavigationexample.navigation.AppNavigation3Controller
import com.example.jetpacknavigationexample.navigation.AppNavigation3Host
import com.example.jetpacknavigationexample.navigation.AppNavigator
import com.example.jetpacknavigationexample.navigation.ProductAppLink
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity(), AppNavigator {

    private lateinit var navigationController: AppNavigation3Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        navigationController = AppNavigation3Controller.create(
            savedTokens = savedInstanceState?.getStringArrayList(KEY_NAVIGATION_STACK)
        )

        if (savedInstanceState == null && ProductAppLink.matches(intent)) {
            navigationController.openProductFlowByAppLink()
        }

        setContent {
            AppNavigation3Host(
                fragmentManager = supportFragmentManager,
                backStack = navigationController.backStack,
                onBack = ::handleBackNavigation
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(KEY_NAVIGATION_STACK, navigationController.saveState())
    }

    override fun openProductFlow() {
        navigationController.openProductFlow()
    }

    override fun openProductFlowByAppLink() {
        navigationController.openProductFlowByAppLink()
    }

    override fun openProductOnboarding() {
        navigationController.openProductOnboarding()
    }

    override fun openProduct() {
        navigationController.openProduct()
    }

    override fun navigateBack() {
        handleBackNavigation()
    }

    private fun handleBackNavigation() {
        if (!navigationController.navigateBack()) {
            finish()
        }
    }

    private companion object {
        private const val KEY_NAVIGATION_STACK = "key_navigation_stack"
    }
}
