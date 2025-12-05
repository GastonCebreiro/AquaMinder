package com.example.aquaminder.feature_weather.data.model.response

import com.example.aquaminder.feature_main.domain.model.stringToAddress
import com.example.aquaminder.feature_weather.data.model.HourlyWeatherNetworkEntity
import com.example.aquaminder.feature_weather.data.model.toDomainModel
import com.example.aquaminder.feature_weather.domain.model.WeatherDomainModel
import com.example.aquaminder.feature_weather.utils.WeatherUtils
import com.google.gson.annotations.SerializedName

data class GetWeatherResponse(
    val status: Int? = null,
    @SerializedName("icon_code")
    val iconCode: Int? = null,
    @SerializedName("temperature")
    val temperature: Double? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("rain_prob")
    val rainProb: Int? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("hourly_weather")
    val hourlyWeather: List<HourlyWeatherNetworkEntity>? = null
)

fun GetWeatherResponse.toDomainModel() = WeatherDomainModel(
    icon = WeatherUtils.codeToIconWeather(iconCode),
    temperature = temperature?.toInt() ?: 0,
    description = description.orEmpty(),
    humidity = humidity ?: 0,
    rainProb = rainProb ?: 0,
    address = stringToAddress(address.orEmpty()),
    hourlyWeather = hourlyWeather?.map { it.toDomainModel() }.orEmpty()
)