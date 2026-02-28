package com.example.jetpacknavigationexample.domain.usecase

import com.example.jetpacknavigationexample.data.repository.ProductFlowRepository
import javax.inject.Inject

class GetProductShortcutRemainingMillisUseCase @Inject constructor(
    private val repository: ProductFlowRepository
) {
    operator fun invoke(): Long = repository.getProductShortcutRemainingMillis()
}
