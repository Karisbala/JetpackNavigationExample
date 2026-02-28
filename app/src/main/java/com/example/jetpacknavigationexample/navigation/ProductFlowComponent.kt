@file:Suppress("UNUSED_PARAMETER")

package com.example.jetpacknavigationexample.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value

class ProductFlowComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = null,
        initialConfiguration = Configuration.Details,
        handleBackButton = false,
        childFactory = ::createChild
    )

    fun openProductOnboarding() {
        if (stack.value.active.instance != Child.Onboarding) {
            navigation.pushNew(Configuration.Onboarding)
        }
    }

    fun openProduct(shouldMarkProductVisit: Boolean = true) {
        when (stack.value.active.instance) {
            Child.Details -> navigation.pushNew(Configuration.Product(shouldMarkProductVisit))
            Child.Onboarding -> navigation.replaceCurrent(
                Configuration.Product(shouldMarkProductVisit)
            )

            is Child.Product -> navigation.replaceCurrent(
                Configuration.Product(shouldMarkProductVisit)
            )
        }
    }

    fun navigateBack(): Boolean {
        if (stack.value.backStack.isEmpty()) {
            return false
        }

        navigation.pop()
        return true
    }

    fun activeChild(): Child = stack.value.active.instance

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.Details -> Child.Details
            Configuration.Onboarding -> Child.Onboarding
            is Configuration.Product -> Child.Product(configuration.shouldMarkProductVisit)
        }
    }

    sealed interface Child {
        data object Details : Child
        data object Onboarding : Child
        data class Product(val shouldMarkProductVisit: Boolean) : Child
    }

    private sealed interface Configuration {
        data object Details : Configuration
        data object Onboarding : Configuration
        data class Product(val shouldMarkProductVisit: Boolean) : Configuration
    }
}
