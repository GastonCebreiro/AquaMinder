package com.example.aquaminder.feature_login.domain.model.request

import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.domain.model.UserDomainModel

data class LoginUserRequestDomainModel(
    var name: String,
    var password: String
)

fun LoginUserRequestDomainModel.toNetworkEntity() = LoginUserRequestNetworkEntity(
    name = name,
    password = password
)