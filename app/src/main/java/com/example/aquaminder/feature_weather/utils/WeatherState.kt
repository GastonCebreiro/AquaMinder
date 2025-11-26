package com.example.aquaminder.feature_weather.utils

import com.example.aquaminder.feature_weather.domain.model.WeatherDomainModel

sealed class WeatherState {
    object Idle : WeatherState()
    data class Success(val weather: WeatherDomainModel) : WeatherState()
    data class Error(val errorMsg: String, val logoId: Int? = null) : WeatherState()
}
