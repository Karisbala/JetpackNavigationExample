package com.example.jetpacknavigationexample.data.repository

interface ProductFlowRepository {
    fun isProductShortcutAvailable(): Boolean
    fun markProductScreenVisited()
}
