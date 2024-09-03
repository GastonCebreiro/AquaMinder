package com.example.aquaminder.feature_login.utils


sealed class LoginState {
    object Idle : LoginState()
    data class Success(val msg: String) : LoginState()
    data class Error(val errorMsg: String, val logoId: Int? = null) : LoginState()
    data class WrongName(val errorMsg: String) : LoginState()
    data class WrongPassword(val errorMsg: String) : LoginState()
    data class UsernameNotFound(val errorMsg: String) : LoginState()
    data class UserSavedValues(val name: String, val password: String) : LoginState()
    data class KeepValues(val isChecked: Boolean) : LoginState()
    data class NewPasswordSent(val msg: String) : LoginState()
}