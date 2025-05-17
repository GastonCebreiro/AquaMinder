package com.example.aquaminder.feature_notifications.domain.use_case

import com.example.aquaminder.feature_notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    fun invoke(): String {
        return repository.getToken()
    }
}