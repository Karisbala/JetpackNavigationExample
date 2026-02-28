package com.example.jetpacknavigationexample

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.arkivanov.decompose.defaultComponentContext
import com.example.jetpacknavigationexample.databinding.ActivityProductBinding
import com.example.jetpacknavigationexample.navigation.AppNavigator
import com.example.jetpacknavigationexample.navigation.ProductAppLink
import com.example.jetpacknavigationexample.navigation.ProductFlowComponent
import com.example.jetpacknavigationexample.ui.details.ProductDetailsFragment
import com.example.jetpacknavigationexample.ui.onboarding.ProductOnboardingFragment
import com.example.jetpacknavigationexample.ui.product.PRODUCT_ARG_SHOULD_MARK_VISIT
import com.example.jetpacknavigationexample.ui.product.ProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity(), AppNavigator {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productFlowComponent: ProductFlowComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        productFlowComponent = ProductFlowComponent(defaultComponentContext())
        restoreNavigationState(savedInstanceState)
        registerBackCallback()
        renderActiveChild(immediate = savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        when (val child = productFlowComponent.activeChild()) {
            ProductFlowComponent.Child.Details -> {
                outState.putString(KEY_ACTIVE_SCREEN, ACTIVE_SCREEN_DETAILS)
            }

            ProductFlowComponent.Child.Onboarding -> {
                outState.putString(KEY_ACTIVE_SCREEN, ACTIVE_SCREEN_ONBOARDING)
            }

            is ProductFlowComponent.Child.Product -> {
                outState.putString(KEY_ACTIVE_SCREEN, ACTIVE_SCREEN_PRODUCT)
                outState.putBoolean(KEY_SHOULD_MARK_PRODUCT_VISIT, child.shouldMarkProductVisit)
            }
        }
    }

    override fun openProductOnboarding() {
        productFlowComponent.openProductOnboarding()
        renderActiveChild()
    }

    override fun openProduct() {
        productFlowComponent.openProduct()
        renderActiveChild()
    }

    override fun navigateBack() {
        if (productFlowComponent.navigateBack()) {
            renderActiveChild()
        } else {
            finish()
        }
    }

    private fun registerBackCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })
    }

    private fun restoreNavigationState(savedInstanceState: Bundle?) {
        when {
            savedInstanceState != null -> restoreFromSavedState(savedInstanceState)
            ProductAppLink.matches(intent) -> productFlowComponent.openProduct(
                shouldMarkProductVisit = false
            )
        }
    }

    private fun restoreFromSavedState(savedInstanceState: Bundle) {
        when (savedInstanceState.getString(KEY_ACTIVE_SCREEN, ACTIVE_SCREEN_DETAILS)) {
            ACTIVE_SCREEN_ONBOARDING -> productFlowComponent.openProductOnboarding()
            ACTIVE_SCREEN_PRODUCT -> productFlowComponent.openProduct(
                shouldMarkProductVisit = savedInstanceState.getBoolean(
                    KEY_SHOULD_MARK_PRODUCT_VISIT,
                    true
                )
            )
        }
    }

    private fun renderActiveChild(immediate: Boolean = false) {
        val fragment = when (val child = productFlowComponent.activeChild()) {
            ProductFlowComponent.Child.Details -> ProductDetailsFragment()
            ProductFlowComponent.Child.Onboarding -> ProductOnboardingFragment()
            is ProductFlowComponent.Child.Product -> ProductFragment.newInstance(
                shouldMarkProductVisit = child.shouldMarkProductVisit
            )
        }

        if (!immediate && isExistingFragmentUpToDate(fragment)) {
            return
        }

        if (immediate) {
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.productFragmentContainer,
                    fragment,
                    fragment::class.java.simpleName
                )
                .commitNow()
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.productFragmentContainer,
                    fragment,
                    fragment::class.java.simpleName
                )
            }
        }
    }

    private fun isExistingFragmentUpToDate(expectedFragment: Fragment): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.productFragmentContainer)
            ?: return false

        if (currentFragment::class != expectedFragment::class) {
            return false
        }

        if (currentFragment is ProductFragment && expectedFragment is ProductFragment) {
            return currentFragment.arguments?.getBoolean(PRODUCT_ARG_SHOULD_MARK_VISIT, true) ==
                expectedFragment.arguments?.getBoolean(PRODUCT_ARG_SHOULD_MARK_VISIT, true)
        }

        return true
    }

    private companion object {
        private const val KEY_ACTIVE_SCREEN = "key_active_screen"
        private const val KEY_SHOULD_MARK_PRODUCT_VISIT = "key_should_mark_product_visit"

        private const val ACTIVE_SCREEN_DETAILS = "details"
        private const val ACTIVE_SCREEN_ONBOARDING = "onboarding"
        private const val ACTIVE_SCREEN_PRODUCT = "product"
    }
}
