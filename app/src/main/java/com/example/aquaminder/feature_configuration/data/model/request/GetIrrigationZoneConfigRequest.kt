package com.example.aquaminder.feature_configuration.data.model.request

data class GetIrrigationZoneConfigRequest(
    val uuid: String
) {

    fun toMap(): Map<String, String> {
        return mapOf("id_equipo" to uuid)
    }
}