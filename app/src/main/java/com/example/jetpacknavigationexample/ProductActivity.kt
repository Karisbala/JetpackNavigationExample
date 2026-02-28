package com.example.jetpacknavigationexample

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.jetpacknavigationexample.databinding.ActivityProductBinding
import com.example.jetpacknavigationexample.navigation.ProductAppLink
import com.example.jetpacknavigationexample.ui.product.PRODUCT_ARG_SHOULD_MARK_VISIT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

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
            openProductFromAppLinkIfNeeded()
        }
    }

    private fun openProductFromAppLinkIfNeeded() {
        if (!ProductAppLink.matches(intent)) {
            return
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.productNavHostFragment) as NavHostFragment

        navHostFragment.navController.navigate(
            R.id.action_productDetailsFragment_to_productFragment,
            bundleOf(PRODUCT_ARG_SHOULD_MARK_VISIT to false)
        )
    }
}
