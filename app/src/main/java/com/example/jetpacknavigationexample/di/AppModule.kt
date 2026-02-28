@file:Suppress("unused")

package com.example.jetpacknavigationexample.di

import android.content.Context
import android.content.SharedPreferences
import com.example.jetpacknavigationexample.data.repository.ProductFlowRepository
import com.example.jetpacknavigationexample.data.repository.SharedPreferencesProductFlowRepository
import com.example.jetpacknavigationexample.data.time.SystemTimeProvider
import com.example.jetpacknavigationexample.data.time.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindProductFlowRepository(
        repository: SharedPreferencesProductFlowRepository
    ): ProductFlowRepository

    @Binds
    @Singleton
    abstract fun bindTimeProvider(
        timeProvider: SystemTimeProvider
    ): TimeProvider

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences = context.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

        private const val PREFERENCES_NAME = "product_flow_preferences"
    }
}
