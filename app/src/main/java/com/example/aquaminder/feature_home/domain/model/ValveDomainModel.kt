package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_configuration.domain.model.LastWatersDomainModel
import com.example.aquaminder.feature_configuration.domain.model.NextWatersDomainModel
import com.example.aquaminder.feature_configuration.domain.model.toNetworkEntity
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
    val isWatering: Boolean,
    val lastWaters: List<LastWatersDomainModel>?,
    val nextWaters: List<NextWatersDomainModel>?,
    val lastHumidity: List<Int>
): Parcelable

fun ValveDomainModel.toNetworkEntity() = ValveNetworkEntity(
    id = id,
    controlMode = controlMode,
    humidityMin = humidityMin,
    humidityMax = humidityMax,
    isWeatherChecked = if(isWeatherChecked) 1 else 0,
    schedule = schedule?.toNetworkEntity(),
    isActive = if(isActive) 1 else 0,
    isWatering = if(isWatering) 1 else 0,
    lastWaters = lastWaters?.map { it.toNetworkEntity() },
    nextWaters = nextWaters?.map { it.toNetworkEntity() },
    lastHumidity = lastHumidity
)