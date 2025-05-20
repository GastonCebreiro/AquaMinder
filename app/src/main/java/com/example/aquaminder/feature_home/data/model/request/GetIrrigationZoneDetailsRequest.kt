package com.example.aquaminder.feature_home.data.model.request

data class GetIrrigationZoneDetailsRequest(
    val uuid: String
) {

    fun toMap(): Map<String, String> {
        return mapOf("id_equipo" to uuid)
    }
}