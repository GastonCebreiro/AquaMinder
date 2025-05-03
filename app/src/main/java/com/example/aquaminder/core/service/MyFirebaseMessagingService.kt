package com.example.aquaminder.core.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.aquaminder.R
import com.example.aquaminder.feature_notifications.di.MyFirebaseMessagingServiceEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("GASTON", "Refreshed token: $token")

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            MyFirebaseMessagingServiceEntryPoint::class.java
        )

        val sendTokenUseCase = entryPoint.sendTokenUseCase()

        CoroutineScope(Dispatchers.IO).launch {
            val isSuccess = sendTokenUseCase.invoke(token)
            Log.d("GASTON", "Token sent: $isSuccess")
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("GASTON", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d("GASTON", "Message data payload: ${remoteMessage.data}")
            // Handle data payload here
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("GASTON", "Message Notification Body: ${it.body}")
            showNotification(it.title ?: "Notification", it.body ?: "")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "fcm_default_channel"

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "FCM Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Firebase Cloud Messaging default channel"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_aquaminder) // <- replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            } else {
                Log.w("GASTON", "Notification permission not granted — notification not shown.")
            }
        }
    }
}