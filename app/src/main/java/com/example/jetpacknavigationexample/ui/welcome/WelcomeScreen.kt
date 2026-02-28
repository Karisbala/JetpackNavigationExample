package com.example.jetpacknavigationexample.ui.welcome

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpacknavigationexample.R

@Composable
fun WelcomeRoute(
    onOpenProductFlow: () -> Unit,
    onOpenProductFlowByAppLink: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                WelcomeEffect.OpenProductFlow -> onOpenProductFlow()
                WelcomeEffect.OpenProductFlowByAppLink -> onOpenProductFlowByAppLink()
            }
        }
    }

    WelcomeScreen(
        uiState = uiState,
        onNextClicked = {
            viewModel.onAction(WelcomeAction.NextClicked)
        },
        onOpenProductByAppLinkClicked = {
            viewModel.onAction(WelcomeAction.OpenProductAppLinkClicked)
        }
    )
}

@Composable
private fun WelcomeScreen(
    uiState: WelcomeUiState,
    onNextClicked: () -> Unit,
    onOpenProductByAppLinkClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.title_welcome),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.welcome_message),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNextClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isNextEnabled
        ) {
            Text(text = stringResource(R.string.action_next))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onOpenProductByAppLinkClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isNextEnabled
        ) {
            Text(text = stringResource(R.string.action_open_product_by_app_link))
        }
    }
}
