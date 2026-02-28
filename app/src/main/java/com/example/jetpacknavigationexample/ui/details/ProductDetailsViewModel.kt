package com.example.jetpacknavigationexample.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpacknavigationexample.domain.usecase.GetProductShortcutRemainingMillisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductShortcutRemainingMillis: GetProductShortcutRemainingMillisUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProductDetailsEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    private var countdownJob: Job? = null

    init {
        refreshShortcutAvailability()
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            ProductDetailsAction.BackClicked -> _effects.tryEmit(ProductDetailsEffect.NavigateBack)
            ProductDetailsAction.ScreenResumed -> {
                refreshShortcutAvailability()
                startCountdown()
            }

            ProductDetailsAction.ScreenStopped -> stopCountdown()
            ProductDetailsAction.NextClicked -> emitNavigationEffect()
        }
    }

    private fun refreshShortcutAvailability() {
        val remainingMillis = getProductShortcutRemainingMillis()
        _uiState.update { currentState ->
            currentState.copy(
                shortcutRemainingSeconds = remainingMillis.toRemainingSeconds()
            )
        }
    }

    private fun emitNavigationEffect() {
        val destination = if (getProductShortcutRemainingMillis() > 0L) {
            ProductDetailsDestination.PRODUCT
        } else {
            ProductDetailsDestination.PRODUCT_ONBOARDING
        }

        _effects.tryEmit(ProductDetailsEffect.Navigate(destination))
    }

    private fun startCountdown() {
        stopCountdown()

        if (getProductShortcutRemainingMillis() <= 0L) {
            return
        }

        countdownJob = viewModelScope.launch {
            while (true) {
                val remainingMillis = getProductShortcutRemainingMillis()
                _uiState.update { currentState ->
                    currentState.copy(
                        shortcutRemainingSeconds = remainingMillis.toRemainingSeconds()
                    )
                }

                if (remainingMillis <= 0L) {
                    countdownJob = null
                    break
                }

                delay(COUNTDOWN_TICK_DELAY_IN_MILLIS)
            }
        }
    }

    private fun stopCountdown() {
        countdownJob?.cancel()
        countdownJob = null
    }

    private fun Long.toRemainingSeconds(): Long {
        return if (this <= 0L) {
            0L
        } else {
            (this + 999L) / 1000L
        }
    }

    private companion object {
        private const val COUNTDOWN_TICK_DELAY_IN_MILLIS = 1_000L
    }
}
