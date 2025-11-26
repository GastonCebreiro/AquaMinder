package com.example.aquaminder.feature_weather.data.model.request

data class GetWeatherRequest(
    val uuid: String
) {

    fun toMap(): Map<String, String> {
        return mapOf("id_equipo" to uuid)
    }
}