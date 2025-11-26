package com.example.aquaminder.feature_weather.data.repository

import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.feature_weather.data.model.HourlyWeatherNetworkEntity
import com.example.aquaminder.feature_weather.data.model.request.GetWeatherRequest
import com.example.aquaminder.feature_weather.data.model.response.GetWeatherResponse
import com.example.aquaminder.feature_weather.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val webService: WebService
): WeatherRepository {
    
    override suspend fun getWeather(request: GetWeatherRequest): GetWeatherResponse {
//        val response: GetWeatherResponse = webService.getWeather(request.toMap())
        // TODO GC DELETE MOCK
        delay(3000)
        val hourlyWeatherList =
            listOf(
                HourlyWeatherNetworkEntity("19:00", 0, 22, 60),
                HourlyWeatherNetworkEntity("20:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("21:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("22:00", 2, 9, 67),
                HourlyWeatherNetworkEntity("23:00", 2, 9, 60),
                HourlyWeatherNetworkEntity("00:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("01:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("02:00", 0, 18, 67),
                HourlyWeatherNetworkEntity("03:00", 0, 22, 60),
                HourlyWeatherNetworkEntity("04:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("05:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("06:00", 2, 9, 67),
                HourlyWeatherNetworkEntity("07:00", 2, 9, 60),
                HourlyWeatherNetworkEntity("08:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("09:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("10:00", 0, 18, 67),
                HourlyWeatherNetworkEntity("11:00", 0, 22, 60),
                HourlyWeatherNetworkEntity("12:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("13:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("14:00", 2, 9, 67),
                HourlyWeatherNetworkEntity("15:00", 2, 9, 60),
                HourlyWeatherNetworkEntity("16:00", 1, 6, 62),
                HourlyWeatherNetworkEntity("17:00", 3, 4, 65),
                HourlyWeatherNetworkEntity("18:00", 0, 18, 67),
            )

        val response = GetWeatherResponse(
            status = 200,
            iconCode = 3,
            temperature = 22,
            description = "Tormenta",
            humidity = 80,
            address = "Av. Juan Bautista Alberdi 1045, C1424 Cdad. Autónoma de Buenos Aires, Argentina",
            hourlyWeather = hourlyWeatherList
        )
        return response
    }

}