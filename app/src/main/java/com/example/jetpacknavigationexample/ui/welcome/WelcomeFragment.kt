package com.example.jetpacknavigationexample.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.jetpacknavigationexample.R
import com.example.jetpacknavigationexample.databinding.FragmentWelcomeBinding
import com.example.jetpacknavigationexample.navigation.ProductAppLink
import com.example.jetpacknavigationexample.ui.common.ViewBindingFragment
import com.example.jetpacknavigationexample.ui.common.collectLatestLifecycleFlow
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
            WelcomeEffect.OpenProductFlow -> {
                findNavController().navigate(R.id.action_welcomeFragment_to_productActivity)
            }

            WelcomeEffect.OpenProductFlowByAppLink -> {
                val appLinkIntent = Intent(Intent.ACTION_VIEW, ProductAppLink.uri).apply {
                    setPackage(requireContext().packageName)
                }
                startActivity(appLinkIntent)
            }
        }
    }
}
