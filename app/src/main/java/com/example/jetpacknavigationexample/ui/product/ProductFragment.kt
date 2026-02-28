package com.example.jetpacknavigationexample.ui.product

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.databinding.FragmentProductBinding
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
import com.example.jetpacknavigationexample.ui.common.requireAppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment :
    ViewBindingFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.inputFirstNumber.doAfterTextChanged { editable ->
            viewModel.onAction(ProductAction.FirstNumberChanged(editable?.toString().orEmpty()))
        }

        binding.inputSecondNumber.doAfterTextChanged { editable ->
            viewModel.onAction(ProductAction.SecondNumberChanged(editable?.toString().orEmpty()))
        }

        binding.buttonCalculate.setOnClickListener {
            viewModel.onAction(ProductAction.CalculateClicked)
        }

        binding.buttonBack.setOnClickListener {
            viewModel.onAction(ProductAction.BackClicked)
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.uiState, ::render)
        collectLatestLifecycleFlow(viewModel.effects, ::handleEffect)
    }

    private fun render(uiState: ProductUiState) {
        val firstNumber = binding.inputFirstNumber.text?.toString().orEmpty()
        if (firstNumber != uiState.firstNumber.value) {
            binding.inputFirstNumber.setText(uiState.firstNumber.value)
            binding.inputFirstNumber.setSelection(binding.inputFirstNumber.text?.length ?: 0)
        }

        val secondNumber = binding.inputSecondNumber.text?.toString().orEmpty()
        if (secondNumber != uiState.secondNumber.value) {
            binding.inputSecondNumber.setText(uiState.secondNumber.value)
            binding.inputSecondNumber.setSelection(binding.inputSecondNumber.text?.length ?: 0)
        }

        binding.layoutFirstNumber.error = uiState.firstNumber.error?.toErrorMessage()
        binding.layoutSecondNumber.error = uiState.secondNumber.error?.toErrorMessage()

        binding.buttonCalculate.isVisible = !uiState.isResultVisible
        binding.textResult.isVisible = uiState.isResultVisible
        binding.textResult.text = getString(
            R.string.product_result,
            uiState.resultText.orEmpty()
        )
    }

    private fun handleEffect(effect: ProductEffect) {
        when (effect) {
            ProductEffect.NavigateBack -> requireAppNavigator().navigateBack()
        }
    }

    private fun ProductInputError.toErrorMessage(): String {
        val stringRes = when (this) {
            ProductInputError.INVALID_NUMBER -> R.string.error_invalid_number
            ProductInputError.TOO_LARGE -> R.string.error_numbers_too_large
        }

        return getString(stringRes)
    }
}
