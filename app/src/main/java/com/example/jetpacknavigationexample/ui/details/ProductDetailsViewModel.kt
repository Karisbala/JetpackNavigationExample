package com.example.jetpacknavigationexample.ui.details

import androidx.lifecycle.ViewModel
import com.example.jetpacknavigationexample.domain.usecase.ShouldSkipOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val shouldSkipOnboarding: ShouldSkipOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProductDetailsEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        refreshShortcutAvailability()
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            ProductDetailsAction.ScreenResumed -> refreshShortcutAvailability()
            ProductDetailsAction.NextClicked -> emitNavigationEffect()
        }
    }

    private fun refreshShortcutAvailability() {
        _uiState.update { currentState ->
            currentState.copy(isShortcutAvailable = shouldSkipOnboarding())
        }
    }

    private fun emitNavigationEffect() {
        val destination = if (shouldSkipOnboarding()) {
            ProductDetailsDestination.PRODUCT
        } else {
            ProductDetailsDestination.PRODUCT_ONBOARDING
        }

        _effects.tryEmit(ProductDetailsEffect.Navigate(destination))
    }
}
