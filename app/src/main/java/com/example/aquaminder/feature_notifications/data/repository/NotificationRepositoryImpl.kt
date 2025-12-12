package com.example.aquaminder.feature_notifications.data.repository

import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferencesUtil
) : NotificationRepository {

    override  fun saveToken(
        token: String
    ) {
        sharedPreferences.setToken(token)
    }

    override  fun getToken(): String {
        return sharedPreferences.getToken()
    }

}

