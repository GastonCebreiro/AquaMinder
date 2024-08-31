package com.example.aquaminder.feature_login.domain.model

import com.example.aquaminder.feature_login.data.local.entity.UserEntity


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