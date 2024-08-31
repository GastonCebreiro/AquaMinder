package com.example.aquaminder.core.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesUtil(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setUserLoggedName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_LOGGED_NAME, name)
        editor.apply()
    }

    fun getUserLoggedName(): String? {
        return sharedPreferences.getString(USER_LOGGED_NAME, "")
    }

    fun setUserLoggedMail(mail: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_LOGGED_MAIL, mail)
        editor.apply()
    }

    fun getUserLoggedMail(): String? {
        return sharedPreferences.getString(USER_LOGGED_MAIL, "")
    }

    fun setUserLoggedPassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_LOGGED_PASSWORD, password)
        editor.apply()
    }

    fun getUserLoggedPassword(): String? {
        return sharedPreferences.getString(USER_LOGGED_PASSWORD, "")
    }

    fun setKeepValues(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGIN_KEEP_VALUES, isChecked)
        editor.apply()
    }

    fun getKeepValues(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_KEEP_VALUES, true)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
        const val USER_LOGGED_NAME = "USER_LOGGED_NAME"
        const val USER_LOGGED_MAIL = "USER_LOGGED_MAIL"
        const val USER_LOGGED_PASSWORD = "USER_LOGGED_PASSWORD"
        const val LOGIN_KEEP_VALUES = "LOGIN_KEEP_VALUES"
    }
}