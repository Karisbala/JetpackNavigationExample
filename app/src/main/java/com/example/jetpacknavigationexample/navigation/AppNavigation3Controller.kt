package com.example.jetpacknavigationexample.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class AppNavigation3Controller private constructor(
    val backStack: NavBackStack<AppDestination>
) {

    val currentDestination: AppDestination
        get() = backStack.last()

    fun openProductFlow() {
        replaceBackStack(
            AppDestination.Welcome,
            AppDestination.ProductDetails
        )
    }

    fun openProductFlowByAppLink() {
        replaceBackStack(
            AppDestination.Welcome,
            AppDestination.ProductDetails,
            AppDestination.Product(shouldMarkProductVisit = false)
        )
    }

    fun openProductOnboarding() {
        if (currentDestination == AppDestination.ProductDetails) {
            backStack.add(AppDestination.ProductOnboarding)
        }
    }

    fun openProduct(shouldMarkProductVisit: Boolean = true) {
        val productDestination = AppDestination.Product(
            shouldMarkProductVisit = shouldMarkProductVisit
        )

        when (currentDestination) {
            AppDestination.Welcome -> replaceBackStack(
                AppDestination.Welcome,
                AppDestination.ProductDetails,
                productDestination
            )

            AppDestination.ProductDetails -> backStack.add(productDestination)
            AppDestination.ProductOnboarding -> {
                backStack.removeAt(backStack.lastIndex)
                backStack.add(productDestination)
            }

            is AppDestination.Product -> {
                backStack[backStack.lastIndex] = productDestination
            }
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.size <= 1) {
            return false
        }

        backStack.removeAt(backStack.lastIndex)
        return true
    }

    fun saveState(): ArrayList<String> {
        return ArrayList(backStack.map(AppDestination::toSavedToken))
    }

    companion object {
        fun create(savedTokens: List<String>? = null): AppNavigation3Controller {
            val tokens = savedTokens
                ?.mapNotNull(AppDestination::fromSavedToken)
                ?.ifEmpty { null }

            val restoredBackStack: NavBackStack<AppDestination> = if (tokens == null) {
                NavBackStack<AppDestination>(AppDestination.Welcome)
            } else {
                NavBackStack<AppDestination>(tokens.first()).apply {
                    tokens.drop(1).forEach(::add)
                }
            }

            return AppNavigation3Controller(restoredBackStack)
        }
    }

    private fun replaceBackStack(vararg destinations: AppDestination) {
        require(destinations.isNotEmpty()) {
            "Navigation stack must contain at least one destination."
        }

        while (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }

        backStack[0] = destinations.first()
        destinations.drop(1).forEach(backStack::add)
    }
}

sealed interface AppDestination : NavKey {
    data object Welcome : AppDestination
    data object ProductDetails : AppDestination
    data object ProductOnboarding : AppDestination
    data class Product(val shouldMarkProductVisit: Boolean) : AppDestination

    fun toSavedToken(): String {
        return when (this) {
            Welcome -> TOKEN_WELCOME
            ProductDetails -> TOKEN_PRODUCT_DETAILS
            ProductOnboarding -> TOKEN_PRODUCT_ONBOARDING
            is Product -> "$TOKEN_PRODUCT_PREFIX$shouldMarkProductVisit"
        }
    }

    companion object {
        fun fromSavedToken(token: String): AppDestination? {
            return when {
                token == TOKEN_WELCOME -> Welcome
                token == TOKEN_PRODUCT_DETAILS -> ProductDetails
                token == TOKEN_PRODUCT_ONBOARDING -> ProductOnboarding
                token.startsWith(TOKEN_PRODUCT_PREFIX) -> Product(
                    shouldMarkProductVisit = token.removePrefix(TOKEN_PRODUCT_PREFIX).toBooleanStrict()
                )

                else -> null
            }
        }

        private const val TOKEN_WELCOME = "welcome"
        private const val TOKEN_PRODUCT_DETAILS = "product_details"
        private const val TOKEN_PRODUCT_ONBOARDING = "product_onboarding"
        private const val TOKEN_PRODUCT_PREFIX = "product:"
    }
}
