package com.example.jetpacknavigationexample.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.databinding.FragmentProductDetailsBinding
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
import com.example.jetpacknavigationexample.ui.common.navigateUpOrBackPress
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

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

    override fun onStop() {
        viewModel.onAction(ProductDetailsAction.ScreenStopped)
        super.onStop()
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            viewModel.onAction(ProductDetailsAction.BackClicked)
        }

        binding.buttonNext.setOnClickListener {
            viewModel.onAction(ProductDetailsAction.NextClicked)
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.uiState, ::render)
        collectLatestLifecycleFlow(viewModel.effects, ::handleEffect)
    }

    private fun render(uiState: ProductDetailsUiState) {
        if (uiState.isShortcutAvailable) {
            binding.textDetailThree.text = getString(
                R.string.product_details_point_three_countdown,
                uiState.shortcutRemainingSeconds.toCountdownText()
            )
        } else {
            binding.textDetailThree.setText(R.string.product_details_point_three_default)
        }
    }

    private fun handleEffect(effect: ProductDetailsEffect) {
        when (effect) {
            ProductDetailsEffect.NavigateBack -> navigateUpOrBackPress()
            is ProductDetailsEffect.Navigate -> {
                when (effect.destination) {
                    ProductDetailsDestination.PRODUCT_ONBOARDING -> findNavController().navigate(
                        R.id.action_productDetailsFragment_to_productOnboardingFragment
                    )

                    ProductDetailsDestination.PRODUCT -> findNavController().navigate(
                        R.id.action_productDetailsFragment_to_productFragment
                    )
                }
            }
        }
    }

    private fun Long.toCountdownText(): String {
        val minutes = this / 60L
        val seconds = this % 60L
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}
