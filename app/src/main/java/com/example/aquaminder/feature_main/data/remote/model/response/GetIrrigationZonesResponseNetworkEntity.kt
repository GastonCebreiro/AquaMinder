package com.example.aquaminder.feature_main.data.remote.model.response

import com.example.aquaminder.core.utils.AppConstants.DEFAULT_COLOR_ID
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_NAME
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_LOGO_ID
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import java.util.UUID

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
                uuid = it.uuid ?: IdentifierUtils.createUUID(),
                name = it.name ?: DEFAULT_IZ_NAME,
                logoId = it.logoId ?: DEFAULT_LOGO_ID,
                colorId = it.colorId ?: DEFAULT_COLOR_ID
            )
        }
    } ?: emptyList()
)