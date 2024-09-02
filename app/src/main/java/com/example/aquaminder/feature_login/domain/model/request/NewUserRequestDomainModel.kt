package com.example.aquaminder.feature_login.domain.model.request

import com.example.aquaminder.feature_login.data.remote.model.request.NewUserRequestNetworkEntity

data class NewUserRequestDomainModel(
    var name: String,
    var mail: String,
    var password: String
)

fun NewUserRequestDomainModel.toNetworkEntity() = NewUserRequestNetworkEntity(
    username = name,
    mail = mail,
    password = password
)