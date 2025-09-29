package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
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
): Parcelable
