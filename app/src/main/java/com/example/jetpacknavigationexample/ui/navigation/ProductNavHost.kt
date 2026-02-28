package com.example.jetpacknavigationexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpacknavigationexample.ui.details.ProductDetailsRoute
import com.example.jetpacknavigationexample.ui.onboarding.ProductOnboardingRoute
import com.example.jetpacknavigationexample.ui.product.PRODUCT_ARG_SHOULD_MARK_VISIT
import com.example.jetpacknavigationexample.ui.product.ProductRoute

@Composable
fun ProductNavHost(
    openProductByAppLinkInitially: Boolean,
    onExitRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    LaunchedEffect(openProductByAppLinkInitially) {
        if (openProductByAppLinkInitially) {
            navController.navigate(productRoute(shouldMarkProductVisit = false))
        }
    }

    NavHost(
        navController = navController,
        startDestination = PRODUCT_DETAILS_ROUTE,
        modifier = modifier
    ) {
        composable(route = PRODUCT_DETAILS_ROUTE) {
            ProductDetailsRoute(
                onNavigateBack = {
                    navController.navigateUpOrExit(onExitRequested)
                },
                onNavigateToOnboarding = {
                    navController.navigate(PRODUCT_ONBOARDING_ROUTE)
                },
                onNavigateToProduct = {
                    navController.navigate(productRoute())
                }
            )
        }

        composable(route = PRODUCT_ONBOARDING_ROUTE) {
            ProductOnboardingRoute(
                onNavigateBack = {
                    navController.navigateUpOrExit(onExitRequested)
                },
                onNavigateToProduct = {
                    navController.navigate(productRoute()) {
                        popUpTo(PRODUCT_ONBOARDING_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = PRODUCT_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(PRODUCT_ARG_SHOULD_MARK_VISIT) {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) {
            ProductRoute(
                onNavigateBack = {
                    navController.navigateUpOrExit(onExitRequested)
                }
            )
        }
    }
}

private fun NavController.navigateUpOrExit(onExitRequested: () -> Unit) {
    if (!navigateUp()) {
        onExitRequested()
    }
}

private fun productRoute(shouldMarkProductVisit: Boolean = true): String {
    return "$PRODUCT_ROUTE?$PRODUCT_ARG_SHOULD_MARK_VISIT=$shouldMarkProductVisit"
}

private const val PRODUCT_DETAILS_ROUTE = "product_details"
private const val PRODUCT_ONBOARDING_ROUTE = "product_onboarding"
private const val PRODUCT_ROUTE = "product"
private const val PRODUCT_ROUTE_PATTERN =
    "$PRODUCT_ROUTE?$PRODUCT_ARG_SHOULD_MARK_VISIT={$PRODUCT_ARG_SHOULD_MARK_VISIT}"
