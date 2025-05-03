package com.example.aquaminder.feature_login.presentation.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.aquaminder.R
import com.example.aquaminder.feature_notifications.domain.use_case.SendTokenUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sendTokenUseCase: SendTokenUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setContentView(R.layout.activity_login)

        fetchFcmToken()
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, getString(R.string.notifications_on), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.notifications_off), Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // FCM SDK (and your app) can post notifications.
                    Log.d("Permission", "Notification permission already granted.")
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show an educational UI explaining why notifications are useful
                    showNotificationPermissionRationaleDialog()
                }

                else -> {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun showNotificationPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notifications_ask_title))
            .setMessage(getString(R.string.notifications_ask_message))
            .setPositiveButton(getString(R.string.notifications_accept)) { _, _ ->
                // Request permission after rationale
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton(getString(R.string.notifications_reject)) { dialog, _ ->
                dialog.dismiss()
                // Optionally inform user notifications won't be available
                Toast.makeText(this, getString(R.string.notifications_off), Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("GASTON", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("GASTON", "FCM token: $token")
//            Toast.makeText(this, "token: $token", Toast.LENGTH_SHORT).show()
            // TODO GC CHECK IF NEEDED THIS SEND EVERY TIME
//            lifecycleScope.launch {
//                val isSuccess = sendTokenUseCase.invoke(token)
//                Log.d("GASTON", "Token sent: $isSuccess")
//            }
        }
    }


}