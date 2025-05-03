package com.example.aquaminder.feature_notifications.domain.repository

import com.example.aquaminder.feature_notifications.domain.model.response.TokenResponseDomainModel


interface NotificationRepository {

    suspend fun sendToken(token: String): TokenResponseDomainModel
}