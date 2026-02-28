package com.example.jetpacknavigationexample.domain.usecase

import kotlin.math.abs
import javax.inject.Inject

class CalculateLeastCommonMultipleUseCase @Inject constructor() {

    operator fun invoke(firstNumber: Long, secondNumber: Long): Long {
        require(firstNumber > 0 && secondNumber > 0) {
            "LCM is defined only for positive numbers."
        }

        val gcd = greatestCommonDivisor(abs(firstNumber), abs(secondNumber))
        return Math.multiplyExact(abs(firstNumber / gcd), abs(secondNumber))
    }

    private tailrec fun greatestCommonDivisor(firstNumber: Long, secondNumber: Long): Long {
        return if (secondNumber == 0L) {
            firstNumber
        } else {
            greatestCommonDivisor(secondNumber, firstNumber % secondNumber)
        }
    }
}
