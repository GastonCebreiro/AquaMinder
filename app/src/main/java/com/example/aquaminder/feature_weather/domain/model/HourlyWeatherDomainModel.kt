package com.example.aquaminder.feature_weather.domain.model

import com.example.aquaminder.R
import java.time.LocalTime

data class HourlyWeatherDomainModel(
    val hour: String, // 16:40
    val icon: IconWeather,
    val temperature: Int,
    val humidity: Int,
    val rainProb: Int,
)

fun HourlyWeatherDomainModel.getColorByIcon(): Int {
    return when (this.icon) {
        IconWeather.SUNNY  -> R.color.weather_sunny_yellow
        IconWeather.CLOUDY -> R.color.weather_cloud_gray
        IconWeather.RAINY  -> R.color.weather_light_blue
        IconWeather.STORMY -> R.color.weather_dark_blue
    }
}

fun HourlyWeatherDomainModel.getTimeFormatted(): String {
    val currentHour = LocalTime.now().hour

    val itemHour = try {
        LocalTime.parse(this.hour).hour
    } catch (e: Exception) {
        return this.hour
    }

    return if (itemHour == currentHour) {
        "Ahora"
    } else {
        this.hour
    }
}

fun HourlyWeatherDomainModel.getTemperatureFormatted(): String =
    "${this.temperature}°C"

fun HourlyWeatherDomainModel.getHumidityFormatted(): String =
    "${this.humidity}%"

fun HourlyWeatherDomainModel.getRainProbFormatted(): String =
    "${this.rainProb}%"
