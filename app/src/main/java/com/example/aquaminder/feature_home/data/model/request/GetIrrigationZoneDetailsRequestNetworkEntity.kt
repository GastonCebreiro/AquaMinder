package com.example.aquaminder.feature_home.data.model.request

import com.google.gson.annotations.SerializedName

data class GetIrrigationZoneDetailsRequestNetworkEntity(
    @SerializedName("id_equipo")
    val uuid: String
)