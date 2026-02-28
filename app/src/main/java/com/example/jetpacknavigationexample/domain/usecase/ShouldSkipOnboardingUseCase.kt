package com.example.jetpacknavigationexample.domain.usecase

import com.example.jetpacknavigationexample.data.repository.ProductFlowRepository
import javax.inject.Inject

class ShouldSkipOnboardingUseCase @Inject constructor(
    private val repository: ProductFlowRepository
) {
    operator fun invoke(): Boolean = repository.isProductShortcutAvailable()
}
