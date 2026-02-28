package com.example.jetpacknavigationexample.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.jetpacknavigationexample.databinding.FragmentWelcomeBinding
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
import com.example.jetpacknavigationexample.ui.common.requireAppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment :
    ViewBindingFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {

    private val viewModel by viewModels<WelcomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.onAction(WelcomeAction.NextClicked)
        }

        binding.buttonOpenProductByAppLink.setOnClickListener {
            viewModel.onAction(WelcomeAction.OpenProductAppLinkClicked)
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.uiState, ::render)
        collectLatestLifecycleFlow(viewModel.effects, ::handleEffect)
    }

    private fun render(uiState: WelcomeUiState) {
        binding.buttonNext.isEnabled = uiState.isNextEnabled
        binding.buttonOpenProductByAppLink.isEnabled = uiState.isNextEnabled
    }

    private fun handleEffect(effect: WelcomeEffect) {
        when (effect) {
            WelcomeEffect.OpenProductFlow -> requireAppNavigator().openProductFlow()
            WelcomeEffect.OpenProductFlowByAppLink -> requireAppNavigator().openProductFlowByAppLink()
        }
    }
}
