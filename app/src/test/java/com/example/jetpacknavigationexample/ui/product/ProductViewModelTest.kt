package com.example.jetpacknavigationexample.ui.product

import androidx.lifecycle.SavedStateHandle
import com.example.jetpacknavigationexample.data.repository.ProductFlowRepository
import com.example.jetpacknavigationexample.domain.usecase.CalculateLeastCommonMultipleUseCase
import com.example.jetpacknavigationexample.domain.usecase.MarkProductVisitedUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductViewModelTest {

    @Test
    fun `marks product visit by default`() {
        val repository = FakeProductFlowRepository()

        ProductViewModel(
            savedStateHandle = SavedStateHandle(),
            calculateLeastCommonMultiple = CalculateLeastCommonMultipleUseCase(),
            markProductVisited = MarkProductVisitedUseCase(repository)
        )

        assertEquals(1, repository.markProductScreenVisitedCalls)
    }

    @Test
    fun `does not mark product visit for app link launch`() {
        val repository = FakeProductFlowRepository()

        ProductViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(PRODUCT_ARG_SHOULD_MARK_VISIT to false)
            ),
            calculateLeastCommonMultiple = CalculateLeastCommonMultipleUseCase(),
            markProductVisited = MarkProductVisitedUseCase(repository)
        )

        assertEquals(0, repository.markProductScreenVisitedCalls)
    }

    private class FakeProductFlowRepository : ProductFlowRepository {
        var markProductScreenVisitedCalls: Int = 0

        override fun getProductShortcutRemainingMillis(): Long = 0L

        override fun isProductShortcutAvailable(): Boolean = false

        override fun markProductScreenVisited() {
            markProductScreenVisitedCalls += 1
        }
    }
}
