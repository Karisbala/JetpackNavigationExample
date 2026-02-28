package com.example.jetpacknavigationexample.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.jetpacknavigationexample.domain.usecase.CalculateLeastCommonMultipleUseCase
import com.example.jetpacknavigationexample.domain.usecase.MarkProductVisitedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calculateLeastCommonMultiple: CalculateLeastCommonMultipleUseCase,
    markProductVisited: MarkProductVisitedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProductEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        val shouldMarkProductVisit = savedStateHandle[PRODUCT_ARG_SHOULD_MARK_VISIT] ?: true
        if (shouldMarkProductVisit) {
            markProductVisited()
        }
    }

    fun onAction(action: ProductAction) {
        when (action) {
            ProductAction.BackClicked -> _effects.tryEmit(ProductEffect.NavigateBack)
            ProductAction.CalculateClicked -> calculateResult()
            is ProductAction.FirstNumberChanged -> updateFirstNumber(action.value)
            is ProductAction.SecondNumberChanged -> updateSecondNumber(action.value)
        }
    }

    private fun updateFirstNumber(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                firstNumber = currentState.firstNumber.copy(
                    value = value,
                    error = null
                ),
                resultText = null
            )
        }
    }

    private fun updateSecondNumber(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                secondNumber = currentState.secondNumber.copy(
                    value = value,
                    error = null
                ),
                resultText = null
            )
        }
    }

    private fun calculateResult() {
        val currentState = _uiState.value
        val firstNumber = currentState.firstNumber.value.toPositiveLongOrNull()
        val secondNumber = currentState.secondNumber.value.toPositiveLongOrNull()

        val firstError = if (firstNumber == null) {
            ProductInputError.INVALID_NUMBER
        } else {
            null
        }

        val secondError = if (secondNumber == null) {
            ProductInputError.INVALID_NUMBER
        } else {
            null
        }

        if (firstError != null || secondError != null) {
            _uiState.update {
                it.copy(
                    firstNumber = it.firstNumber.copy(error = firstError),
                    secondNumber = it.secondNumber.copy(error = secondError),
                    resultText = null
                )
            }
            return
        }

        val validFirstNumber = checkNotNull(firstNumber)
        val validSecondNumber = checkNotNull(secondNumber)

        val result = runCatching {
            calculateLeastCommonMultiple(validFirstNumber, validSecondNumber)
        }.getOrElse {
            _uiState.update {
                it.copy(
                    firstNumber = it.firstNumber.copy(error = ProductInputError.TOO_LARGE),
                    secondNumber = it.secondNumber.copy(error = ProductInputError.TOO_LARGE),
                    resultText = null
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                firstNumber = it.firstNumber.copy(error = null),
                secondNumber = it.secondNumber.copy(error = null),
                resultText = result.toString()
            )
        }
    }

    private fun String.toPositiveLongOrNull(): Long? {
        val parsedValue = toLongOrNull() ?: return null
        return parsedValue.takeIf { it > 0 }
    }
}
