package com.example.aquaminder.feature_main.data.remote.model.response

import com.example.aquaminder.core.utils.AppConstants.DEFAULT_COLOR_ID
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_ID
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_NAME
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_LOGO_ID
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils
import com.google.gson.annotations.SerializedName

data class GetIrrigationZonesResponseNetworkEntity(
    val status: Int? = null,
    @SerializedName("equipos")
    val irrigationZones: List<IrrigationZoneNetworkEntity>? = null
)

fun GetIrrigationZonesResponseNetworkEntity.toDomainModel() = GetIrrigationZonesResponseDomainModel(
    status = status ?: -1,
    irrigationZones = irrigationZones?.let { zones ->
        zones.map {
            IrrigationZoneDomainModel(
                uuid = it.id ?: DEFAULT_IZ_ID,
                name = it.name ?: DEFAULT_IZ_NAME,
                logoId = it.logoId ?: DEFAULT_LOGO_ID,
                colorId = IrrigationZoneUtils.getColorByLogo(it.logoId ?: DEFAULT_LOGO_ID)
            )
        }
    } ?: emptyList()
)