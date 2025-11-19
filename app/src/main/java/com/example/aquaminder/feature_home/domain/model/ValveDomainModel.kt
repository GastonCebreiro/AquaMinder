package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_configuration.presentation.fragments.LastWatersDomainModel
import com.example.aquaminder.feature_configuration.presentation.fragments.toNetworkEntity
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValveDomainModel(
    val id: Int,
    val controlMode: ControlMode?,
    val isWeatherChecked: Boolean,
    val humidityMin: Int,
    val humidityMax: Int,
    val schedule: ScheduleDomainModel?,
    val isActive: Boolean,
    val isWatering: Boolean?,
    val lastWaters: List<LastWatersDomainModel>?,
    val lastHumidity: List<Int>
): Parcelable

fun ValveDomainModel.toNetworkEntity() = ValveNetworkEntity(
    id = id,
    controlMode = controlMode,
    humidityMin = humidityMin,
    humidityMax = humidityMax,
    isWeatherChecked = isWeatherChecked,
    schedule = schedule?.toNetworkEntity(),
    isActive = isActive,
    isWatering = isWatering,
    lastWaters = lastWaters?.map { it.toNetworkEntity() },
    lastHumidity = lastHumidity
)