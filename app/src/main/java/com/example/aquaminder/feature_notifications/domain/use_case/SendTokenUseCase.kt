package com.example.aquaminder.feature_notifications.domain.use_case

import android.util.Log
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.core.utils.SoundManager
import com.example.aquaminder.feature_notifications.domain.model.response.TokenResponseDomainModel
import com.example.aquaminder.feature_notifications.domain.repository.NotificationRepository
import java.io.IOException
import javax.inject.Inject

class SendTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend fun invoke(token: String): Boolean {
        try {
            val response = repository.sendToken(token)
            return when (response.status) {
                STATUS_OK -> {
                    true
                }

                else -> {
                    false
                }
            }
        } catch (e: IOException) {
            Log.e("GASTON", "SendTokenUseCase IOException Error: ${e.message}")
        } catch (e: Exception) {
            Log.e("GASTON", "SendTokenUseCase Exception Error: ${e.message}")
        }
        return false
    }
}