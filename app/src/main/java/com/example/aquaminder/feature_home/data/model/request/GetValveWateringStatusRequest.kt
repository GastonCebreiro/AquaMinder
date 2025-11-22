package com.example.aquaminder.feature_home.data.model.request

data class GetValveWateringStatusRequest(
    val uuid: String,   // id_equipo
    val valveId: Int    // id_valvula
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "id_equipo" to uuid,
            "id_valvula" to valveId.toString()
        )
    }
}
