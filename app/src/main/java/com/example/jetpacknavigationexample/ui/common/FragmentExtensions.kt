package com.example.jetpacknavigationexample.ui.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.jetpacknavigationexample.navigation.AppNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Fragment.requireAppNavigator(): AppNavigator {
    return requireActivity() as? AppNavigator
        ?: error("Host activity must implement AppNavigator.")
}

fun <T> Fragment.collectLatestLifecycleFlow(
    flow: Flow<T>,
    collector: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collector)
        }
    }
}
