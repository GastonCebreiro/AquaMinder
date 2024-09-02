package com.example.aquaminder.feature_login.utils


sealed class NewUserState {
    object Idle : NewUserState()
    data class Success(val msg: String) : NewUserState()
    data class Error(val errorMsg: String) : NewUserState()
    data class WrongName(val errorMsg: String) : NewUserState()
    data class WrongMail(val errorMsg: String) : NewUserState()
    data class WrongPassword(val errorMsg: String) : NewUserState()

}