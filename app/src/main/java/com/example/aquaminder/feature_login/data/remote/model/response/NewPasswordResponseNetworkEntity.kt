package com.example.aquaminder.feature_login.data.remote.model.response

import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel

data class NewPasswordResponseNetworkEntity(
    val status: Int? = null,
    val message: String? = null
)

fun NewPasswordResponseNetworkEntity.toDomainModel() = NewPasswordResponseDomainModel(
        status = status ?: -1,
        message = message.orEmpty()
)