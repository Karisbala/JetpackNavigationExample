package com.example.jetpacknavigationexample.data.time

import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}