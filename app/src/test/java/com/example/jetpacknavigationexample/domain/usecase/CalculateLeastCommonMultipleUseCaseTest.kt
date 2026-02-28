package com.example.jetpacknavigationexample.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateLeastCommonMultipleUseCaseTest {

    private val useCase = CalculateLeastCommonMultipleUseCase()

    @Test
    fun `returns least common multiple for two positive numbers`() {
        val result = useCase(firstNumber = 6, secondNumber = 8)

        assertEquals(24L, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when any number is zero or negative`() {
        useCase(firstNumber = 0, secondNumber = 8)
    }
}
