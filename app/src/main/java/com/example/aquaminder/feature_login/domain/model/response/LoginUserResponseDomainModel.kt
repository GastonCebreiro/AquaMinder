package com.example.aquaminder.feature_login.domain.model.response

import com.example.aquaminder.feature_login.domain.model.UserDomainModel

data class LoginUserResponseDomainModel(
    val status: Int,
    val message: String,
    val user: UserDomainModel
)