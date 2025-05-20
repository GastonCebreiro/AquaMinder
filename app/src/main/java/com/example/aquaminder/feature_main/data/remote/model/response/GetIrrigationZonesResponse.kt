package com.example.aquaminder.feature_main.data.remote.model.response

import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.google.gson.annotations.SerializedName

data class GetIrrigationZonesResponse(
    val status: Int? = null,
    @SerializedName("equipos")
    val irrigationZones: List<IrrigationZoneNetworkEntity>? = null
)