package com.example.aquaminder.feature_login.utils

import com.example.aquaminder.feature_login.domain.model.UserDomainModel

sealed class InsertUserEvent {
    data class Success(val user: UserDomainModel) : InsertUserEvent()
    data class Error(val errorMsg: String) : InsertUserEvent()
}