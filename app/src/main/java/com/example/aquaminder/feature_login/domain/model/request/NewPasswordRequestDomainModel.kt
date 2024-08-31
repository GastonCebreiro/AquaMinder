package com.example.aquaminder.feature_login.domain.model.request

import com.example.aquaminder.feature_login.data.remote.model.request.NewPasswordRequestNetworkEntity

data class NewPasswordRequestDomainModel(
    val username: String
)

fun NewPasswordRequestDomainModel.toNetworkEntity() = NewPasswordRequestNetworkEntity(
    username = username
)