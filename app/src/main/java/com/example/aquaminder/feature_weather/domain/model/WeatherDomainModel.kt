package com.example.aquaminder.feature_weather.domain.model

import com.example.aquaminder.R
import com.example.aquaminder.feature_main.domain.model.Address

data class WeatherDomainModel(
    val icon: IconWeather,
    val temperature: Int,
    val description: String,
    val humidity: Int,
    val address: Address,
    val hourlyWeather: List<HourlyWeatherDomainModel>,
)

fun WeatherDomainModel.getTemperatureFormatted(): String =
    "${this.temperature}°C"

fun WeatherDomainModel.getAddressFormatted(): String =
    "${this.address.street} ${this.address.number}, ${this.address.city}"

fun WeatherDomainModel.getHumidityFormatted(): String =
    "${this.humidity}%"

fun WeatherDomainModel.getColorByIcon(): Int {
    return when (this.icon) {
        IconWeather.SUNNY  -> R.color.weather_sunny_yellow
        IconWeather.CLOUDY -> R.color.weather_cloud_gray
        IconWeather.RAINY  -> R.color.weather_light_blue
        IconWeather.STORMY -> R.color.weather_dark_blue
    }
}