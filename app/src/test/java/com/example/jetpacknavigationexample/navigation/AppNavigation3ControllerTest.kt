package com.example.jetpacknavigationexample.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppNavigation3ControllerTest {

    @Test
    fun `open product flow moves from welcome to product details`() {
        val controller = AppNavigation3Controller.create()

        controller.openProductFlow()

        assertEquals(AppDestination.ProductDetails, controller.currentDestination)
        assertTrue(controller.navigateBack())
        assertEquals(AppDestination.Welcome, controller.currentDestination)
        assertFalse(controller.navigateBack())
    }

    @Test
    fun `open product replaces onboarding and back returns to product details`() {
        val controller = AppNavigation3Controller.create()

        controller.openProductFlow()
        controller.openProductOnboarding()
        controller.openProduct()

        assertEquals(
            AppDestination.Product(shouldMarkProductVisit = true),
            controller.currentDestination
        )
        assertTrue(controller.navigateBack())
        assertEquals(AppDestination.ProductDetails, controller.currentDestination)
    }

    @Test
    fun `saved state preserves app link stack`() {
        val initialController = AppNavigation3Controller.create().apply {
            openProductFlowByAppLink()
        }

        val restoredController = AppNavigation3Controller.create(initialController.saveState())

        assertEquals(
            AppDestination.Product(shouldMarkProductVisit = false),
            restoredController.currentDestination
        )
        assertTrue(restoredController.navigateBack())
        assertEquals(AppDestination.ProductDetails, restoredController.currentDestination)
        assertTrue(restoredController.navigateBack())
        assertEquals(AppDestination.Welcome, restoredController.currentDestination)
        assertFalse(restoredController.navigateBack())
    }
}
