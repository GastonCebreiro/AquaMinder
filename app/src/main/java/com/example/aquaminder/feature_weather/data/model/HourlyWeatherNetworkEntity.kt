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
    val temperature: Double? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("rain_prob")
    val rainProb: Int? = null,
)

fun HourlyWeatherNetworkEntity.toDomainModel() = HourlyWeatherDomainModel(
    hour = hour.orEmpty(),
    icon = WeatherUtils.codeToIconWeather(iconCode),
    temperature = temperature?.toInt() ?: 0,
    humidity = humidity ?: 0,
    rainProb = rainProb ?: 0,
)