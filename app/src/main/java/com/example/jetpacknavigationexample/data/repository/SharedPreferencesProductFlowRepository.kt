package com.example.jetpacknavigationexample.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.jetpacknavigationexample.data.time.TimeProvider
import javax.inject.Inject

class SharedPreferencesProductFlowRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val timeProvider: TimeProvider
) : ProductFlowRepository {

    override fun isProductShortcutAvailable(): Boolean {
        val lastVisitedAt = sharedPreferences.getLong(KEY_LAST_PRODUCT_VISIT, NO_VISIT_RECORDED)
        if (lastVisitedAt == NO_VISIT_RECORDED) {
            return false
        }

        val elapsedTime = timeProvider.currentTimeMillis() - lastVisitedAt
        return elapsedTime in 0..SHORTCUT_WINDOW_IN_MILLIS
    }

    override fun markProductScreenVisited() {
        sharedPreferences.edit {
            putLong(KEY_LAST_PRODUCT_VISIT, timeProvider.currentTimeMillis())
        }
    }

    private companion object {
        private const val KEY_LAST_PRODUCT_VISIT = "key_last_product_visit"
        private const val NO_VISIT_RECORDED = -1L
        private const val SHORTCUT_WINDOW_IN_MILLIS = 60_000L
    }
}
