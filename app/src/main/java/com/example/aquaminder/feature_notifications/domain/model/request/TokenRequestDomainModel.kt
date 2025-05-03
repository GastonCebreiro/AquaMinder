package com.example.aquaminder.feature_notifications.domain.model.request

import com.example.aquaminder.feature_notifications.data.remote.model.request.TokenRequestNetworkEntity

data class SendTokenRequestDomainModel(
    var token: String,
)

fun SendTokenRequestDomainModel.toNetworkEntity() = TokenRequestNetworkEntity(
    token = token
)