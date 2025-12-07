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
        val response: GetWeatherResponse = webService.getWeather(request.toMap())
        // TODO GC DELETE MOCK
//        delay(3000)
//        val hourlyWeatherList =
//            listOf(
//                HourlyWeatherNetworkEntity("00:00", 1, 6.0, 62, 30),
//                HourlyWeatherNetworkEntity("01:00", 3, 4.0, 65, 20),
//                HourlyWeatherNetworkEntity("02:00", 0, 18.0, 67, 10),
//                HourlyWeatherNetworkEntity("03:00", 0, 22.0, 60, 50),
//                HourlyWeatherNetworkEntity("04:00", 1, 6.0, 62, 60),
//                HourlyWeatherNetworkEntity("05:00", 3, 4.0, 65, 20),
//                HourlyWeatherNetworkEntity("06:00", 2, 9.0, 67, 10),
//                HourlyWeatherNetworkEntity("07:00", 2, 9.0, 60, 0),
//                HourlyWeatherNetworkEntity("08:00", 1, 6.0, 62, 0),
//                HourlyWeatherNetworkEntity("09:00", 3, 4.0, 65, 100),
//                HourlyWeatherNetworkEntity("10:00", 0, 18.0, 67, 80),
//                HourlyWeatherNetworkEntity("11:00", 0, 22.0, 60, 80),
//                HourlyWeatherNetworkEntity("12:00", 1, 6.0, 62, 20),
//                HourlyWeatherNetworkEntity("13:00", 3, 4.0, 65, 30),
//                HourlyWeatherNetworkEntity("14:00", 2, 9.0, 67, 30),
//                HourlyWeatherNetworkEntity("15:00", 2, 9.0, 60, 30),
//                HourlyWeatherNetworkEntity("16:00", 1, 6.0, 62, 30),
//                HourlyWeatherNetworkEntity("17:00", 3, 4.0, 65, 30),
//                HourlyWeatherNetworkEntity("18:00", 0, 18.0, 67, 30),
//                HourlyWeatherNetworkEntity("19:00", 0, 22.0, 60, 30),
//                HourlyWeatherNetworkEntity("20:00", 1, 6.0, 62, 30),
//                HourlyWeatherNetworkEntity("21:00", 3, 4.0, 65, 30),
//                HourlyWeatherNetworkEntity("22:00", 2, 9.0, 67, 30),
//                HourlyWeatherNetworkEntity("23:00", 2, 9.0, 60, 30),
//            )
//
//        val response = GetWeatherResponse(
//            status = 200,
//            iconCode = 3,
//            temperature = 22.0,
//            description = "Tormenta",
//            humidity = 80,
//            rainProb = 100,
//            address = "Av. Juan Bautista Alberdi 1045, C1424 Cdad. Autónoma de Buenos Aires, Argentina",
//            hourlyWeather = hourlyWeatherList
//        )
        return response
    }

}