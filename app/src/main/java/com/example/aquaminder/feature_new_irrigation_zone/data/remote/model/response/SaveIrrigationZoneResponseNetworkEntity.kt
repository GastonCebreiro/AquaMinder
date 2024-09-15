package com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response

import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel

data class SaveIrrigationZoneResponseNetworkEntity(
    val status: Int? = null,
)

fun SaveIrrigationZoneResponseNetworkEntity.toDomainModel() = SaveIrrigationZoneResponseDomainModel(
    status = status ?: -1
)