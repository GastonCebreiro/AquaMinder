package com.example.aquaminder.feature_home.data.model.request

import com.google.gson.annotations.SerializedName

data class ManualWateringRequest(
    @SerializedName("id_equipo")
    val uuid: String,
    @SerializedName("id_valvula")
    val valveId: Int,
    @SerializedName("water")
    val water: Boolean
)