package com.example.jetpacknavigationexample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.jetpacknavigationexample.databinding.ActivityProductBinding
import com.example.jetpacknavigationexample.navigation.AppNavigator
import com.example.jetpacknavigationexample.ui.details.ProductDetailsFragment
import com.example.jetpacknavigationexample.ui.onboarding.ProductOnboardingFragment
import com.example.jetpacknavigationexample.ui.product.ProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity(), AppNavigator {

    private lateinit var binding: ActivityProductBinding

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

        if (savedInstanceState == null) {
            replaceFragment(
                fragment = ProductDetailsFragment(),
                addToBackStack = false
            )
        }
    }

    override fun openProductOnboarding() {
        replaceFragment(
            fragment = ProductOnboardingFragment(),
            addToBackStack = true,
            backStackName = PRODUCT_ONBOARDING_BACK_STACK
        )
    }

    override fun openProduct() {
        supportFragmentManager.popBackStackImmediate(
            PRODUCT_ONBOARDING_BACK_STACK,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        replaceFragment(
            fragment = ProductFragment(),
            addToBackStack = true,
            backStackName = PRODUCT_BACK_STACK
        )
    }

    override fun navigateBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        backStackName: String? = null
    ) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.productFragmentContainer, fragment, fragment::class.java.simpleName)
            if (addToBackStack) {
                addToBackStack(backStackName)
            }
        }
    }

    private companion object {
        private const val PRODUCT_ONBOARDING_BACK_STACK = "product_onboarding_back_stack"
        private const val PRODUCT_BACK_STACK = "product_back_stack"
    }
}
