package com.example.aquaminder.feature_main.data.remote.model.response

import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel

data class GetIrrigationZonesResponseNetworkEntity(
    val status: Int? = null,
    val uuid: String? = null,
    val name: String? = null,
    var logoId: Int? = null,
    var colorId: Int? = null
)

fun GetIrrigationZonesResponseNetworkEntity.toDomainModel() = GetIrrigationZonesResponseDomainModel(
    status = status ?: -1,
    uuid = uuid.orEmpty(),
    name = name.orEmpty(),
    logoId = logoId ?: -1,
    colorId = colorId ?: -1
)