package com.example.aquaminder.feature_notifications.di

import com.example.aquaminder.feature_login.data.repository.UserRepositoryImpl
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_notifications.data.repository.NotificationRepositoryImpl
import com.example.aquaminder.feature_notifications.domain.repository.NotificationRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @dagger.Provides
    fun providesNotificationRepository(repositoryImpl: NotificationRepositoryImpl): NotificationRepository =
        repositoryImpl
}