package com.example.aquaminder.feature_notifications.domain.repository

import com.example.aquaminder.feature_notifications.domain.model.response.TokenResponseDomainModel


interface NotificationRepository {

     fun saveToken(token: String)
     fun getToken(): String
}