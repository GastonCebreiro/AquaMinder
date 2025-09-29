package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.gson.annotations.SerializedName

data class ValveNetworkEntity(
    @SerializedName("id_valvula")
    val id: Int? = null,
    @SerializedName("modo_control")
    val controlMode: ControlMode? = null,
    @SerializedName("humedad_minima")
    val humidityMin: Int? = null,
    @SerializedName("humedad_maxima")
    val humidityMax: Int? = null,
    @SerializedName("verClima")
    val isWeatherChecked: Boolean? = null,
    @SerializedName("schedule")
    val schedule: ScheduleNetworkEntity? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
)

fun ValveNetworkEntity.toDomainModel() = ValveDomainModel(
    id = id ?: -1,
    controlMode = controlMode ?: ControlMode.SCHEDULED,
    humidityMin = humidityMin ?: 0,
    humidityMax = humidityMax ?: 100,
    isWeatherChecked = isWeatherChecked ?: false,
    schedule = schedule?.toDomainModel(),
    isActive = isActive ?: false
)
