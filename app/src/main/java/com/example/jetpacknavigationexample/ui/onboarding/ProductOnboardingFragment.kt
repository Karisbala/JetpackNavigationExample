package com.example.jetpacknavigationexample.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.databinding.FragmentProductOnboardingBinding
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
import com.example.jetpacknavigationexample.ui.common.navigateUpOrBackPress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductOnboardingFragment :
    ViewBindingFragment<FragmentProductOnboardingBinding>(FragmentProductOnboardingBinding::inflate) {

    private val viewModel by viewModels<ProductOnboardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            viewModel.onAction(ProductOnboardingAction.BackClicked)
        }

        binding.buttonNext.setOnClickListener {
            viewModel.onAction(ProductOnboardingAction.NextClicked)
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.uiState, ::render)
        collectLatestLifecycleFlow(viewModel.effects, ::handleEffect)
    }

    private fun render(uiState: ProductOnboardingUiState) {
        binding.buttonNext.isEnabled = uiState.isReady
    }

    private fun handleEffect(effect: ProductOnboardingEffect) {
        when (effect) {
            ProductOnboardingEffect.NavigateBack -> navigateUpOrBackPress()
            ProductOnboardingEffect.NavigateToProduct -> findNavController().navigate(
                R.id.action_productOnboardingFragment_to_productFragment
            )
        }
    }
}
