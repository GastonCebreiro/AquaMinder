package com.example.aquaminder.feature_login.utils

import com.example.aquaminder.feature_login.domain.model.UserDomainModel

sealed class LoginEvent {
    data class Success(val user: UserDomainModel, val msg: String) : LoginEvent()
    data class Error(val errorMsg: String) : LoginEvent()
    data class WrongPassword(val errorMsg: String) : LoginEvent()
    data class UsernameNotFound(val errorMsg: String) : LoginEvent()
}