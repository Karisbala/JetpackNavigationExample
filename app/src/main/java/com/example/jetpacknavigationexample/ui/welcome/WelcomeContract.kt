package com.example.jetpacknavigationexample.ui.welcome

data class WelcomeUiState(
    val isNextEnabled: Boolean = true
)

sealed interface WelcomeAction {
    object NextClicked : WelcomeAction
}

sealed interface WelcomeEffect {
    object OpenProductFlow : WelcomeEffect
}
