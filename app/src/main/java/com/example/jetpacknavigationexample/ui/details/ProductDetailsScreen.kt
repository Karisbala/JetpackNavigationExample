package com.example.jetpacknavigationexample.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.ui.common.ObserveLifecycleEvent
import java.util.Locale

@Composable
fun ProductDetailsRoute(
    onNavigateBack: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToProduct: () -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveLifecycleEvent { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.onAction(ProductDetailsAction.ScreenResumed)
            Lifecycle.Event.ON_STOP -> viewModel.onAction(ProductDetailsAction.ScreenStopped)
            else -> Unit
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProductDetailsEffect.NavigateBack -> onNavigateBack()
                is ProductDetailsEffect.Navigate -> {
                    when (effect.destination) {
                        ProductDetailsDestination.PRODUCT_ONBOARDING -> onNavigateToOnboarding()
                        ProductDetailsDestination.PRODUCT -> onNavigateToProduct()
                    }
                }
            }
        }
    }

    ProductDetailsScreen(
        uiState = uiState,
        onBackClicked = {
            viewModel.onAction(ProductDetailsAction.BackClicked)
        },
        onNextClicked = {
            viewModel.onAction(ProductDetailsAction.NextClicked)
        }
    )
}

@Composable
private fun ProductDetailsScreen(
    uiState: ProductDetailsUiState,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    val thirdText = if (uiState.isShortcutAvailable) {
        stringResource(
            R.string.product_details_point_three_countdown,
            uiState.shortcutRemainingSeconds.toCountdownText()
        )
    } else {
        stringResource(R.string.product_details_point_three_default)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.title_product_details),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.product_details_point_one),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.product_details_point_two),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = thirdText,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.action_next))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.action_back))
        }
    }
}

private fun Long.toCountdownText(): String {
    val minutes = this / 60L
    val seconds = this % 60L
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}
