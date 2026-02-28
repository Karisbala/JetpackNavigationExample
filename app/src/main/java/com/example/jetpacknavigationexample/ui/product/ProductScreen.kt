package com.example.jetpacknavigationexample.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpacknavigationexample.R

@Composable
fun ProductRoute(
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProductEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    ProductScreen(
        uiState = uiState,
        onFirstNumberChanged = { value ->
            viewModel.onAction(ProductAction.FirstNumberChanged(value))
        },
        onSecondNumberChanged = { value ->
            viewModel.onAction(ProductAction.SecondNumberChanged(value))
        },
        onCalculateClicked = {
            viewModel.onAction(ProductAction.CalculateClicked)
        },
        onBackClicked = {
            viewModel.onAction(ProductAction.BackClicked)
        }
    )
}

@Composable
private fun ProductScreen(
    uiState: ProductUiState,
    onFirstNumberChanged: (String) -> Unit,
    onSecondNumberChanged: (String) -> Unit,
    onCalculateClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val firstNumberErrorRes = uiState.firstNumber.error?.toErrorMessageRes()
    val secondNumberErrorRes = uiState.secondNumber.error?.toErrorMessageRes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.title_product),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = uiState.firstNumber.value,
            onValueChange = onFirstNumberChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            label = {
                Text(text = stringResource(R.string.hint_first_number))
            },
            singleLine = true,
            isError = firstNumberErrorRes != null,
            supportingText = {
                if (firstNumberErrorRes != null) {
                    Text(text = stringResource(firstNumberErrorRes))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = uiState.secondNumber.value,
            onValueChange = onSecondNumberChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = {
                Text(text = stringResource(R.string.hint_second_number))
            },
            singleLine = true,
            isError = secondNumberErrorRes != null,
            supportingText = {
                if (secondNumberErrorRes != null) {
                    Text(text = stringResource(secondNumberErrorRes))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        if (uiState.isResultVisible) {
            Text(
                text = stringResource(
                    R.string.product_result,
                    uiState.resultText.orEmpty()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onCalculateClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(text = stringResource(R.string.action_calculate))
            }
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

private fun ProductInputError.toErrorMessageRes(): Int {
    return when (this) {
        ProductInputError.INVALID_NUMBER -> R.string.error_invalid_number
        ProductInputError.TOO_LARGE -> R.string.error_numbers_too_large
    }
}
