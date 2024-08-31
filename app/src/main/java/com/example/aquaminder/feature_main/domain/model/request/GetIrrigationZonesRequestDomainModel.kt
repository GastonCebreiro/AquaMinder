package com.example.aquaminder.feature_main.domain.model.request

import com.example.aquaminder.feature_main.data.remote.model.request.GetIrrigationZonesRequestNetworkEntity

data class GetIrrigationZonesRequestDomainModel(
    val username: String
)

fun GetIrrigationZonesRequestDomainModel.toNetworkEntity() = GetIrrigationZonesRequestNetworkEntity(
    username = username
)