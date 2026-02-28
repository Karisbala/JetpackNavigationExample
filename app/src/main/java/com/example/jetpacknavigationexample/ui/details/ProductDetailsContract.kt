package com.example.jetpacknavigationexample.ui.details

data class ProductDetailsUiState(
    val isShortcutAvailable: Boolean = false
)

sealed interface ProductDetailsAction {
    object ScreenResumed : ProductDetailsAction
    object BackClicked : ProductDetailsAction
    object NextClicked : ProductDetailsAction
}

sealed interface ProductDetailsEffect {
    object NavigateBack : ProductDetailsEffect
    data class Navigate(val destination: ProductDetailsDestination) : ProductDetailsEffect
}

enum class ProductDetailsDestination {
    PRODUCT_ONBOARDING,
    PRODUCT
}
