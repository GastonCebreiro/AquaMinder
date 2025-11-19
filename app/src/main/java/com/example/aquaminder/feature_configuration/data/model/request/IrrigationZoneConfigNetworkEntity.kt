package com.example.aquaminder.feature_configuration.data.model.request

import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.google.gson.annotations.SerializedName

data class IrrigationZoneConfigNetworkEntity(
    @SerializedName("id_equipo")
    var id: String? = null,
    @SerializedName("valves")
    var valves: List<ValveNetworkEntity>? = null
)
