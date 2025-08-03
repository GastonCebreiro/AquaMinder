package com.example.aquaminder.feature_weather.utils

import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel

sealed class WeatherState {
    object Idle : WeatherState()
    data class Success(val weather: IrrigationZoneConfigDomainModel) : WeatherState()
    data class Error(val errorMsg: String) : WeatherState()
}
