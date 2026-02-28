package com.example.jetpacknavigationexample.data.repository

interface ProductFlowRepository {
    fun getProductShortcutRemainingMillis(): Long
    fun isProductShortcutAvailable(): Boolean
    fun markProductScreenVisited()
}
