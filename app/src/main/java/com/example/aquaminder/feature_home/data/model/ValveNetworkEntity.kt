package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.gson.annotations.SerializedName

data class ValveNetworkEntity(
    @SerializedName("id_valvula")
    val id: Int? = null,
    @SerializedName("modo_control")
    val controlMode: ControlMode? = null,
    @SerializedName("humidity_min")
    val humidityMin: Int? = null,
    @SerializedName("humidity_max")
    val humidityMax: Int? = null,
    @SerializedName("weather_enable")
    val isWeatherChecked: Boolean? = null,
    @SerializedName("schedule")
    val schedule: ScheduleNetworkEntity? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("is_watering")
    val isWatering: Boolean? = null,
    @SerializedName("last_waters")
    val lastWaters: List<LastWatersNetworkEntity>? = null,
    @SerializedName("last_humidity")
    val lastHumidity: List<Int>? = null,
)

fun ValveNetworkEntity.toDomainModel() = ValveDomainModel(
    id = id ?: -1,
    controlMode = controlMode ?: ControlMode.SCHEDULED,
    humidityMin = humidityMin ?: 0,
    humidityMax = humidityMax ?: 100,
    isWeatherChecked = isWeatherChecked ?: false,
    schedule = schedule?.toDomainModel(),
    isActive = isActive ?: false,
    isWatering = isWatering ?: false,
    lastWaters = lastWaters?.map {
        it.toDomainModel()
    }.orEmpty(),
    lastHumidity = lastHumidity ?: emptyList()
)
