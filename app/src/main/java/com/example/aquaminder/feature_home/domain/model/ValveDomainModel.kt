package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_configuration.presentation.fragments.LastWatersDomainModel
import com.example.aquaminder.feature_home.data.model.LastWatersNetworkEntity
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
