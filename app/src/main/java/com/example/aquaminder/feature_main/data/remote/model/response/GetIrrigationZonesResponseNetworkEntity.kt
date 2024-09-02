package com.example.aquaminder.feature_main.data.remote.model.response

import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.model.response.IrrigationZoneDomainModel

data class GetIrrigationZonesResponseNetworkEntity(
    val status: Int? = null,
    val irrigationZones: List<IrrigationZoneNetworkEntity>? = null
)

data class IrrigationZoneNetworkEntity(
    val uuid: String? = null,
    val name: String? = null,
    var logoId: Int? = null,
    var colorId: Int? = null
)

fun GetIrrigationZonesResponseNetworkEntity.toDomainModel() = GetIrrigationZonesResponseDomainModel(
    status = status ?: -1,
    irrigationZones = irrigationZones?.let { zones ->
        zones.map {
            IrrigationZoneDomainModel(
                uuid = it.uuid.orEmpty(),
                name = it.name.orEmpty(),
                logoId = it.logoId ?: -1,
                colorId = it.colorId ?: -1
            )
        }
    } ?: emptyList()
)