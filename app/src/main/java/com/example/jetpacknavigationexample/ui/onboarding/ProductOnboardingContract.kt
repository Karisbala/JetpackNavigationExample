package com.example.jetpacknavigationexample.ui.onboarding

data class ProductOnboardingUiState(
    val isReady: Boolean = true
)

sealed interface ProductOnboardingAction {
    object BackClicked : ProductOnboardingAction
    object NextClicked : ProductOnboardingAction
}

sealed interface ProductOnboardingEffect {
    object NavigateBack : ProductOnboardingEffect
    object NavigateToProduct : ProductOnboardingEffect
}
