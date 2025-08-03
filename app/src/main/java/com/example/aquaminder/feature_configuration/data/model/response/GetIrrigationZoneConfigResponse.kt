package com.example.aquaminder.feature_configuration.data.model.response

import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.example.aquaminder.feature_home.data.model.toDomainModel
import com.google.gson.annotations.SerializedName

data class GetIrrigationZoneConfigResponse(
    val status: Int? = null,
    @SerializedName("id")
    val uuid: String? = null,
    @SerializedName("medirHumedad")
    val isCheckHumidityEnabled: Boolean? = null,
    @SerializedName("revisarClima")
    val isCheckWeatherEnabled: Boolean? = null,
    @SerializedName("valves")
    var valves: List<ValveNetworkEntity>? = null
)

fun GetIrrigationZoneConfigResponse.toDomainModel() = IrrigationZoneConfigDomainModel(
    uuid = uuid.orEmpty(),
    isCheckHumidityEnabled = isCheckHumidityEnabled ?: false,
    isCheckWeatherEnabled = isCheckWeatherEnabled ?: false,
    valves = valves?.map {
        it.toDomainModel()
    }.orEmpty()
)
