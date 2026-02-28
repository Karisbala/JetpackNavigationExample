package com.example.jetpacknavigationexample.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpacknavigationexample.R

@Composable
fun ProductOnboardingRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProduct: () -> Unit,
    viewModel: ProductOnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProductOnboardingEffect.NavigateBack -> onNavigateBack()
                ProductOnboardingEffect.NavigateToProduct -> onNavigateToProduct()
            }
        }
    }

    ProductOnboardingScreen(
        uiState = uiState,
        onBackClicked = {
            viewModel.onAction(ProductOnboardingAction.BackClicked)
        },
        onNextClicked = {
            viewModel.onAction(ProductOnboardingAction.NextClicked)
        }
    )
}

@Composable
private fun ProductOnboardingScreen(
    uiState: ProductOnboardingUiState,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.title_product_onboarding),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.product_onboarding_message),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isReady
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
