package com.example.aquaminder.feature_weather.data.model

import com.example.aquaminder.feature_weather.domain.model.HourlyWeatherDomainModel
import com.example.aquaminder.feature_weather.utils.WeatherUtils
import com.google.gson.annotations.SerializedName

data class HourlyWeatherNetworkEntity(
    @SerializedName("time")
    val hour: String? = null, // 16:40
    @SerializedName("icon_code")
    val iconCode: Int? = null,
    @SerializedName("temperature")
    val temperature: Int? = null,
    @SerializedName("humidity")
    val humidity: Int? = null
)

fun HourlyWeatherNetworkEntity.toDomainModel() = HourlyWeatherDomainModel(
    hour = hour.orEmpty(),
    icon = WeatherUtils.codeToIconWeather(iconCode),
    temperature = temperature ?: 0,
    humidity = humidity ?: 0
)