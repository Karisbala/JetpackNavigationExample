package com.example.jetpacknavigationexample.ui.product

data class ProductInputFieldState(
    val value: String = "",
    val error: ProductInputError? = null
)

data class ProductUiState(
    val firstNumber: ProductInputFieldState = ProductInputFieldState(),
    val secondNumber: ProductInputFieldState = ProductInputFieldState(),
    val resultText: String? = null
) {
    val isResultVisible: Boolean
        get() = resultText != null
}

sealed interface ProductAction {
    data class FirstNumberChanged(val value: String) : ProductAction
    data class SecondNumberChanged(val value: String) : ProductAction
    object CalculateClicked : ProductAction
    object BackClicked : ProductAction
}

sealed interface ProductEffect {
    object NavigateBack : ProductEffect
}

enum class ProductInputError {
    INVALID_NUMBER,
    TOO_LARGE
}
