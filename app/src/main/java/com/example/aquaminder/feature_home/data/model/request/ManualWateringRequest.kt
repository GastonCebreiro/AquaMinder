package com.example.aquaminder.feature_home.data.model.request

import com.google.gson.annotations.SerializedName

data class ManualWateringRequest(
    @SerializedName("id_equipo")
    val uuid: String,   // id_equipo
    @SerializedName("water")
    val water: Boolean
)