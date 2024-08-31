package com.example.aquaminder.feature_login.utils

sealed class NewPasswordEvent {
    data class Success(val msg: String) : NewPasswordEvent()
    data class Error(val errorMsg: String) : NewPasswordEvent()
    data class UsernameNotFound(val errorMsg: String) : NewPasswordEvent()
}