package com.example.aquaminder.feature_main.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.feature_login.presentation.activities.LoginActivity
import com.example.aquaminder.feature_main.presentation.fragments.IrrigationZonesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment?
        navHost?.let {
            it.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                when(fragment){
                    is IrrigationZonesFragment -> {
                        DialogUtils.showLogoutDialog(
                            context = this,
                            onAcceptAction = {
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        )
                    }
                    else -> {
                        super.onBackPressed()
                    }
                }
            }
        }
    }
}