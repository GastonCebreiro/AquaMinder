package com.example.aquaminder.feature_configuration.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_main.domain.model.Address
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneConfigDomainModel(
    val uuid: String,
    val valves: List<ValveDomainModel>,
    val isCheckHumidityEnabled: Boolean,
    val isCheckWeatherEnabled: Boolean,
): Parcelable
