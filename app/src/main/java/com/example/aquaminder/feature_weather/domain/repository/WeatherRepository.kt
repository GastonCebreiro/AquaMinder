package com.example.aquaminder.feature_weather.domain.repository

import com.example.aquaminder.feature_weather.data.model.request.GetWeatherRequest
import com.example.aquaminder.feature_weather.data.model.response.GetWeatherResponse

interface WeatherRepository {

    suspend fun getWeather(request: GetWeatherRequest): GetWeatherResponse
}