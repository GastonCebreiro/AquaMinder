package com.example.aquaminder.feature_login.domain.model.request

import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity

data class LoginUserRequestDomainModel(
    var name: String,
    var password: String,
    var token: String
)

fun LoginUserRequestDomainModel.toNetworkEntity() = LoginUserRequestNetworkEntity(
    username = name,
    password = password,
    token = token
)