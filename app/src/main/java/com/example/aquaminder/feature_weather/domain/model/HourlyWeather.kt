package com.example.aquaminder.feature_weather.domain.model

data class HourlyWeather(
    val hour: String,
    val icon: IconWeather,
    val temperature: String,
    val humidity: String
)
