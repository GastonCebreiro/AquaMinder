package com.example.aquaminder.feature_notifications.di

import com.example.aquaminder.feature_notifications.domain.use_case.SendTokenUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MyFirebaseMessagingServiceEntryPoint {
    fun sendTokenUseCase(): SendTokenUseCase
}