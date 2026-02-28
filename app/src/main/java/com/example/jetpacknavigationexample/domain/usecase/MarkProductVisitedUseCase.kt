package com.example.jetpacknavigationexample.domain.usecase

import com.example.jetpacknavigationexample.data.repository.ProductFlowRepository
import javax.inject.Inject

class MarkProductVisitedUseCase @Inject constructor(
    private val repository: ProductFlowRepository
) {
    operator fun invoke() = repository.markProductScreenVisited()
}
