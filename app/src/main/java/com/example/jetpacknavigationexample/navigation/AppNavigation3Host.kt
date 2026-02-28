package com.example.jetpacknavigationexample.navigation

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commitNow
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.jetpacknavigationexample.ui.details.ProductDetailsFragment
import com.example.jetpacknavigationexample.ui.onboarding.ProductOnboardingFragment
import com.example.jetpacknavigationexample.ui.product.ProductFragment
import com.example.jetpacknavigationexample.ui.welcome.WelcomeFragment

@Composable
fun AppNavigation3Host(
    fragmentManager: FragmentManager,
    backStack: List<AppDestination>,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = onBack,
            entryProvider = entryProvider<AppDestination>(
                { destination ->
                    NavEntry(destination) {
                        FragmentDestinationHost(
                            fragmentManager = fragmentManager,
                            destination = destination
                        )
                    }
                },
                {}
            )
        )
    }
}

@Composable
private fun FragmentDestinationHost(
    fragmentManager: FragmentManager,
    destination: AppDestination
) {
    val fragmentTag = remember(destination) {
        "app_navigation:${destination.toSavedToken()}"
    }
    val containerId = remember(destination) {
        View.generateViewId()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FragmentContainerView(context).apply {
                id = containerId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    )

    DisposableEffect(fragmentManager, fragmentTag, containerId, destination) {
        if (!fragmentManager.isDestroyed && !fragmentManager.isStateSaved) {
            val existingFragment = fragmentManager.findFragmentByTag(fragmentTag)

            if (existingFragment == null) {
                fragmentManager.commitNow {
                    setReorderingAllowed(true)
                    replace(containerId, destination.toFragment(), fragmentTag)
                }
            }
        }

        onDispose {
            if (!fragmentManager.isDestroyed && !fragmentManager.isStateSaved) {
                val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) ?: return@onDispose

                fragmentManager.commitNow {
                    setReorderingAllowed(true)
                    remove(existingFragment)
                }
            }
        }
    }
}

private fun AppDestination.toFragment(): Fragment {
    return when (this) {
        AppDestination.Welcome -> WelcomeFragment()
        AppDestination.ProductDetails -> ProductDetailsFragment()
        AppDestination.ProductOnboarding -> ProductOnboardingFragment()
        is AppDestination.Product -> ProductFragment.newInstance(
            shouldMarkProductVisit = shouldMarkProductVisit
        )
    }
}
