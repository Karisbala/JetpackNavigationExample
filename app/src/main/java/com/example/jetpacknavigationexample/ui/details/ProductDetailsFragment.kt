package com.example.jetpacknavigationexample.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.databinding.FragmentProductDetailsBinding
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
import com.example.jetpacknavigationexample.ui.common.requireAppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment :
    ViewBindingFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate) {

    private val viewModel by viewModels<ProductDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(ProductDetailsAction.ScreenResumed)
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.onAction(ProductDetailsAction.NextClicked)
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.uiState, ::render)
        collectLatestLifecycleFlow(viewModel.effects, ::handleEffect)
    }

    private fun render(uiState: ProductDetailsUiState) {
        val messageRes = if (uiState.isShortcutAvailable) {
            R.string.product_details_point_three_shortcut
        } else {
            R.string.product_details_point_three_default
        }

        binding.textDetailThree.setText(messageRes)
    }

    private fun handleEffect(effect: ProductDetailsEffect) {
        when (effect) {
            is ProductDetailsEffect.Navigate -> {
                when (effect.destination) {
                    ProductDetailsDestination.PRODUCT_ONBOARDING ->
                        requireAppNavigator().openProductOnboarding()

                    ProductDetailsDestination.PRODUCT -> requireAppNavigator().openProduct()
                }
            }
        }
    }
}
