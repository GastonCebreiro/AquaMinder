package com.example.aquaminder.feature_notifications.data.remote.model.response

import com.example.aquaminder.feature_notifications.domain.model.response.TokenResponseDomainModel

data class TokenResponseNetworkEntity(
    val status: Int? = null
)

fun TokenResponseNetworkEntity.toDomainModel() = TokenResponseDomainModel(
    status = status ?: -1
)