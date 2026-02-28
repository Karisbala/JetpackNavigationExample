package com.example.jetpacknavigationexample.ui.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<WelcomeEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun onAction(action: WelcomeAction) {
        when (action) {
            WelcomeAction.NextClicked -> _effects.tryEmit(WelcomeEffect.OpenProductFlow)
            WelcomeAction.OpenProductAppLinkClicked ->
                _effects.tryEmit(WelcomeEffect.OpenProductFlowByAppLink)
        }
    }
}
