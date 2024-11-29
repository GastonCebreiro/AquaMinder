package com.example.aquaminder.feature_home.domain.model.request

import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequestNetworkEntity


data class GetIrrigationZoneDetailsRequestDomainModel(
    val uuid: String
)

fun GetIrrigationZoneDetailsRequestDomainModel.toNetworkEntity() = GetIrrigationZoneDetailsRequestNetworkEntity(
    uuid = uuid
)