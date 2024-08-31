package com.example.aquaminder.feature_login.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aquaminder.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

}