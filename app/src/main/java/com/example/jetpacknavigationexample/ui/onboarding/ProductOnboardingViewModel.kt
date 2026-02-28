package com.example.jetpacknavigationexample.ui.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProductOnboardingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProductOnboardingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProductOnboardingEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun onAction(action: ProductOnboardingAction) {
        when (action) {
            ProductOnboardingAction.BackClicked ->
                _effects.tryEmit(ProductOnboardingEffect.NavigateBack)

            ProductOnboardingAction.NextClicked ->
                _effects.tryEmit(ProductOnboardingEffect.NavigateToProduct)
        }
    }
}
