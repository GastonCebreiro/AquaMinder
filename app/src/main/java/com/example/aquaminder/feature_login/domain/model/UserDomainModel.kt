package com.example.aquaminder.feature_login.domain.model

import com.example.aquaminder.feature_login.data.local.entity.UserEntity
import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.request.NewUserRequestNetworkEntity


data class UserDomainModel(
    var name: String,
    var mail: String,
    var password: String
)

fun UserDomainModel.toEntity() = UserEntity(
    name = name,
    mail = mail,
    password = password
)

