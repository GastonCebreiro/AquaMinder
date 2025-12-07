package com.example.aquaminder.feature_main.data.remote.model

import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_ID
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_NAME
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_LOGO_ID
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.domain.model.Location
import com.example.aquaminder.feature_main.domain.model.stringToAddress
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils
import com.google.gson.annotations.SerializedName

data class IrrigationZoneNetworkEntity(
    val id: String? = null,
    val name: String? = null,
    @SerializedName("logo_id")
    val logoId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
)

fun IrrigationZoneNetworkEntity.toDomainModel() = IrrigationZoneDomainModel(
    uuid = id ?: DEFAULT_IZ_ID,
    name = name ?: DEFAULT_IZ_NAME,
    logoId = IrrigationZoneUtils.getLogoById(logoId ?: DEFAULT_LOGO_ID),
    colorId =  IrrigationZoneUtils.getColorById(logoId ?: DEFAULT_LOGO_ID),
    location = Location(
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        address = stringToAddress(address.orEmpty()),
    )
)