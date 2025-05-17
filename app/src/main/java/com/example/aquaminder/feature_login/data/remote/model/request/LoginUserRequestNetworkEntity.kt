package com.example.aquaminder.feature_login.data.remote.model.request

data class LoginUserRequestNetworkEntity(
    var username: String?,
    var password: String?,
    var token: String?
)