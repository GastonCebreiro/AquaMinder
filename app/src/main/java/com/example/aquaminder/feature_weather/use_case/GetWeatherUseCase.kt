package com.example.aquaminder.feature_weather.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_weather.data.model.request.GetWeatherRequest
import com.example.aquaminder.feature_weather.data.model.response.toDomainModel
import com.example.aquaminder.feature_weather.domain.model.WeatherDomainModel
import com.example.aquaminder.feature_weather.domain.repository.WeatherRepository
import java.io.IOException
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
){
    suspend operator fun invoke(
        uuid: String
    ): ResultEvent<WeatherDomainModel> {
        val request = GetWeatherRequest(uuid)
        return try {
            val response = weatherRepository.getWeather(request)
            when (response.status) {
                STATUS_OK -> {
                    ResultEvent.Success(
                        response.toDomainModel()
                    )
                }

                else -> {
                    ResultEvent.Error(AppError.WeatherUnavailable)
                }
            }
        } catch (e: IOException) {
            ResultEvent.Error(AppError.NetworkError)
        } catch (e: Exception) {
            ResultEvent.Error(AppError.GenericError(e.message.orEmpty()))
        }
    }
}